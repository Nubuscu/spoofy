package me.nubuscu.spoofy

import android.app.Activity
import android.content.Intent
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
import me.nubuscu.spoofy.utils.DataManager
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


        val searchResultsList = findViewById<RecyclerView>(R.id.searchResultsList).apply {
            layoutManager = LinearLayoutManager(context)
        }
        val searchBar = findViewById<EditText>(R.id.searchBar).apply {
            setOnEditorActionListener { textView: TextView, i: Int, _ ->
                if (i == EditorInfo.IME_ACTION_DONE) {
                    queryList(spotify, textView.text.toString(), searchResultsList)
                }
                false
            }
        }
    }

    private fun queryList(spotify: SpotifyService, queryText: String, out: RecyclerView) {
        spotify.searchArtists(queryText, object : Callback<ArtistsPager> {
            override fun success(resultsPager: ArtistsPager?, response: Response?) {
                if (resultsPager == null) {
                    Toast.makeText(this@ArtistSearchActivity, "Could not find any results", Toast.LENGTH_SHORT).show()
                    return
                }
                val artists = resultsPager.artists.items.toList()
                out.adapter = ArtistAdapter(artists) { a ->
                    returnWithSelected(a)
                }
            }

            override fun failure(error: RetrofitError?) {
                Log.e("search", "unable to complete search", error)
            }
        })
    }

    private fun returnWithSelected(artist: Artist) {
        val intent = Intent().apply {
            putExtra("artistId", artist.id)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}