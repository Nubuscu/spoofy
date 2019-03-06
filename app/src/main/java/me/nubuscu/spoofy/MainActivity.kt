package me.nubuscu.spoofy

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kaaes.spotify.webapi.android.SpotifyApi
import kaaes.spotify.webapi.android.SpotifyService
import kaaes.spotify.webapi.android.models.Artist
import kaaes.spotify.webapi.android.models.ArtistsPager
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

class MainActivity : AppCompatActivity() {
    private lateinit var token: String
    private lateinit var spotify: SpotifyService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        token = intent.getStringExtra("spotifyToken")
        val api = SpotifyApi()
        api.setAccessToken(token)
        spotify = api.service
        findArtist("Tesseract")
    }

    fun findArtist(artistName: String) {
        /**
         * searches for the given string as an artist
         */
        spotify.searchArtists(artistName, object : Callback<ArtistsPager> {
            override fun success(t: ArtistsPager?, response: Response?) {
                //TODO assign the pager to a list view or something
            }

            override fun failure(error: RetrofitError?) {
                Log.e("findArtist", "bother")
            }
        })
    }

    fun getArtist(artistId: String) {
        spotify.getArtist(artistId, object : Callback<Artist> {
            override fun success(t: Artist?, response: Response?) {
                //TODO set the centre of the map to be this artist
                //TODO trigger search for related artists
            }

            override fun failure(error: RetrofitError?) {
                //TODO empty the map? display an error message
            }
        })
    }
}
