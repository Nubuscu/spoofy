package me.nubuscu.spoofy

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kaaes.spotify.webapi.android.SpotifyService
import kaaes.spotify.webapi.android.models.Artist
import kaaes.spotify.webapi.android.models.ArtistsPager
import me.nubuscu.spoofy.view.ArtistAdapter
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

class ArtistSearchActivity : AppCompatActivity() {

    private lateinit var spotify: SpotifyService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_search)
        spotify = DataManager.instance.spotify


        val searchBar = findViewById<EditText>(R.id.searchBar)
        val searchResultsList = findViewById<RecyclerView>(R.id.searchResultsList)
        searchResultsList.layoutManager = LinearLayoutManager(this)

        searchBar.setOnEditorActionListener { textView: TextView, i: Int, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                queryList(spotify, textView.text.toString(), searchResultsList)
            }
            false

        }
        //TODO make onClick handler to return to previous fragment with an artist id
    }

    private fun queryList(spotify: SpotifyService, queryText: String, out: RecyclerView) {
        spotify.searchArtists(queryText, object : Callback<ArtistsPager> {
            override fun success(resultsPager: ArtistsPager?, response: Response?) {
                if (resultsPager == null) {
                    Toast.makeText(this@ArtistSearchActivity, "Could not find any results", Toast.LENGTH_SHORT).show()
                    return
                }
                val artists = resultsPager.artists.items.toList()
                out.adapter = ArtistAdapter(artists)
            }

            override fun failure(error: RetrofitError?) {
                Log.e("search", "unable to complete search", error)
            }

        })
    }

    private fun returnWithSelected(artist: Artist) {
        //TODO create an intent to click the back button and take this artist with you
    }
}