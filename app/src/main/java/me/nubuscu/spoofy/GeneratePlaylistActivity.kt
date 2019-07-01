package me.nubuscu.spoofy

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.ProgressBar
import kaaes.spotify.webapi.android.models.Pager
import kaaes.spotify.webapi.android.models.PlaylistTrack
import kaaes.spotify.webapi.android.models.Recommendations
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.nubuscu.spoofy.utils.DataManager
import me.nubuscu.spoofy.view.SongAdapter
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response
import kotlin.random.Random

class GeneratePlaylistActivity : AppCompatActivity() {
    private val spotify = DataManager.instance.spotify
    private val songs: MutableList<PlaylistTrack> = mutableListOf() //accumulate all the songs in the playlist
    private lateinit var recommendationsList: RecyclerView
    private lateinit var loadingProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_playlist)
        val playlistId = intent.getStringExtra("playlistId")
        loadingProgressBar = findViewById(R.id.gp_progress_bar)
        loadingProgressBar.visibility = ProgressBar.VISIBLE
        recommendationsList = findViewById(R.id.recommendations_list)
        recommendationsList.layoutManager = LinearLayoutManager(this)

        getAllSongsAsync(playlistId).invokeOnCompletion {
            generatePlaylist(songs)
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
                pager = spotify.getPlaylistTracks(userId, playlistId, mapOf(Pair("offset", songs.size)))
                songs.addAll(pager.items)
            }
            getSongsJob.join()
        } while (pager.total > songs.size)
    }

    private fun generatePlaylist(songs: List<PlaylistTrack>) = GlobalScope.launch {
        val numSeedObjects = 5
        val mostFreqArtists = mostFrequentArtists(songs, 0, 5)
        val someSongs: List<String> = songs.map { it.track.id }.shuffled().subList(0, 5)
        val numArtists: Int = Random.nextInt(0, numSeedObjects)
        val numSongs = numSeedObjects - numArtists

        val seedArtists = mostFreqArtists.shuffled().subList(0, numArtists).joinToString(",")
        val seedTracks = someSongs.subList(0, numSongs).joinToString(",")
        spotify.getRecommendations(mapOf(Pair("seed_artists", seedArtists), Pair("seed_tracks", seedTracks)),
            object : Callback<Recommendations> {
                override fun success(rec: Recommendations, response: Response) {
                    recommendationsList.adapter = SongAdapter(rec.tracks)
                    (recommendationsList.adapter as SongAdapter).notifyDataSetChanged()
                    loadingProgressBar.visibility = ProgressBar.GONE
                }

                override fun failure(error: RetrofitError) {
                    Log.e("spotify", "failed to get recommendations", error)
                    loadingProgressBar.visibility = ProgressBar.GONE
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
}