package me.nubuscu.spoofy

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.widget.EditText
import kaaes.spotify.webapi.android.SpotifyService

class ArtistSearchActivity : AppCompatActivity() {

    private lateinit var spotify: SpotifyService

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_artist_search)
        spotify = DataManager.instance.spotify


        val searchBar = findViewById<EditText>(R.id.searchBar)
        val searchResultsList = findViewById<RecyclerView>(R.id.searchResultsList)

        //TODO make handler to query spotify for artists
        //TODO populate the result list with spotify results
        //TODO make onClick handler to return to previous fragment with an artist id
    }
}