package me.nubuscu.spoofy

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import kaaes.spotify.webapi.android.SpotifyError
import kaaes.spotify.webapi.android.models.Pager
import kaaes.spotify.webapi.android.models.PlaylistTrack
import kaaes.spotify.webapi.android.models.Recommendations
import kaaes.spotify.webapi.android.models.Track
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.nubuscu.spoofy.utils.DataManager
import me.nubuscu.spoofy.view.SongAdapter
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response
import kotlin.random.Random

class GeneratePlaylistActivity : AppCompatActivity() {
    private val playlistName = "Spoofy loves you too"
    private val spotify = DataManager.instance.spotify
    private val sourceSongs: MutableList<PlaylistTrack> = mutableListOf() //accumulate all the songs in the playlist
    private var recommendedSongs: MutableList<Track> = mutableListOf()
        set(value) {
            field = value
            recommendationsList.adapter = SongAdapter(field)
            (recommendationsList.adapter as SongAdapter).notifyDataSetChanged()
            loadingProgressBar.visibility = ProgressBar.GONE
        }
    private lateinit var recommendationsList: RecyclerView
    private lateinit var loadingProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_playlist)
        val playlistId = intent.getStringExtra("playlistId")
        loadingProgressBar = findViewById(R.id.gp_progress_bar)
        loadingProgressBar.visibility = ProgressBar.VISIBLE
        recommendationsList = findViewById(R.id.recommendations_list)
        val makePlaylistButton: Button = findViewById(R.id.make_playlist_button)
        makePlaylistButton.setOnClickListener {
            makePlaylistOf(recommendedSongs, playlistName)
        }
        recommendationsList.layoutManager = LinearLayoutManager(this)

        getAllSongsAsync(playlistId).invokeOnCompletion {
            generatePlaylist(sourceSongs)
        }
    }

    private fun getAllSongsAsync(playlistId: String) = GlobalScope.launch {
        var userId = ""
        var pager: Pager<PlaylistTrack> = Pager()

        val getIdJob = launch {
            userId = spotify.me.id
        }
        getIdJob.join()
        do {
            val getSongsJob = launch {
                pager = spotify.getPlaylistTracks(userId, playlistId, mapOf(Pair("offset", sourceSongs.size)))
                sourceSongs.addAll(pager.items)
            }
            getSongsJob.join()
        } while (pager.total > sourceSongs.size)
    }

    private fun generatePlaylist(songs: List<PlaylistTrack>) = GlobalScope.launch {
        val numSeedObjects = 5
        val mostFreqArtists = mostFrequentArtists(songs, 0, 5)
        val someSongs: List<String> = songs.map { it.track.id }.shuffled().subList(0, 5)
        val numArtists: Int = Random.nextInt(0, numSeedObjects)
        val numSongs = numSeedObjects - numArtists

        val seedArtists = mostFreqArtists.shuffled().subList(0, numArtists).joinToString(",")
        val seedTracks = someSongs.subList(0, numSongs).joinToString(",")
        spotify.getRecommendations(
            mapOf(
                Pair("seed_artists", seedArtists),
                Pair("seed_tracks", seedTracks),
                Pair("limit", 50)
            ),
            object : Callback<Recommendations> {
                override fun success(rec: Recommendations, response: Response) {
                    recommendedSongs = rec.tracks
                }

                override fun failure(error: RetrofitError) {
                    Log.e("spotify", "failed to get recommendations", error)
                    recommendedSongs = mutableListOf()
                }
            })
    }

    /**
     * Finds the most frequently reoccurring artists in a list of songs
     * @param songs a list of songs to iterate over
     * @param freqLimit an inclusive lower bound of how many times an artist can appear before being included in the
     * return value
     * @param mapLengthLimit maximum length of the return value. The actual value may be less
     * @return a list of artist ids in order of most frequent to least. This has the above limits applied to it.
     */
    private fun mostFrequentArtists(songs: List<PlaylistTrack>, freqLimit: Int, mapLengthLimit: Int): List<String> {
        val artistFrequency: MutableMap<String, Int> = mutableMapOf()
        songs.forEach { playlistTrack ->
            playlistTrack.track.artists.forEach { artist ->
                artistFrequency[artist.id] = artistFrequency.getOrPut(artist.id, { 0 }) + 1
            }
        }
        return artistFrequency
            .filter { it.value >= freqLimit }
            .toList() //makes a list of pairs which can be decomposed as (key, value)
            .sortedBy { (_, value) -> value }
            .map { (key, _) -> key }.subList(0, mapLengthLimit)
    }

    private fun makePlaylistOf(songs: List<Track>, name: String) = GlobalScope.launch {
        val userId = spotify.me.id
        try {

            val playlistId = spotify.createPlaylist(userId, mapOf(Pair("name", name))).id
            spotify.addTracksToPlaylist(
                userId,
                playlistId,
                mapOf(), //query params
                mapOf(Pair("uris", songs.map { it.uri }))
            )
            openInSpotify(playlistId)
        } catch (e: RetrofitError) {
            Log.d("FOO", "something went wrong", SpotifyError.fromRetrofitError(e))
        }
    }

    private fun openInSpotify(playlistId: String) {
        try {
            val openInSpotifyIntent = Intent(Intent.ACTION_VIEW)
            openInSpotifyIntent.data = Uri.parse("spotify:playlist:$playlistId")
            openInSpotifyIntent.putExtra(
                Intent.EXTRA_REFERRER,
                Uri.parse("android-app://$packageName")
            )
            startActivity(openInSpotifyIntent)
        } catch (e: ActivityNotFoundException) {
            val appPackageName = "com.spotify.music"
            val referrer = "adjust_campaign=$packageName&adjust_tracker=ndjczk&utm_source=adjust_preinstall"
            try {
                val uri = Uri.parse("market://details")
                    .buildUpon()
                    .appendQueryParameter("id", appPackageName)
                    .appendQueryParameter("referrer", referrer)
                    .build()
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            } catch (ignored: ActivityNotFoundException) {
                val uri = Uri.parse("https://play.google.com/store/apps/details")
                    .buildUpon()
                    .appendQueryParameter("id", appPackageName)
                    .appendQueryParameter("referrer", referrer)
                    .build()
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }

        }
    }
}