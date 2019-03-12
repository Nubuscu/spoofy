package me.nubuscu.spoofy

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import kaaes.spotify.webapi.android.SpotifyApi
import kaaes.spotify.webapi.android.SpotifyService
import me.nubuscu.spoofy.classes.TimeRange

class MainActivity : AppCompatActivity() {
    private lateinit var token: String
    private lateinit var spotify: SpotifyService
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        token = intent.getStringExtra("spotifyToken")
        val api = SpotifyApi()
        api.setAccessToken(token)
        spotify = api.service

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // set item as selected to persist highlight
            menuItem.isChecked = true
            // close drawer when item is tapped
            drawerLayout.closeDrawers()

            // Add code here to update the UI based on the item selected
            // For example, swap UI fragments here

            true
        }
        populateListView(TimeRange.LONG_TERM, 10, 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun populateListView(range: TimeRange, limit: Int, offset: Int) {
        val listView: ListView = findViewById(R.id.recent_list)
        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            arrayOf("hello there", "second thing", "some third thing")
        )
        listView.adapter = adapter
    }

//    fun findArtist(artistName: String) {
//        /**
//         * searches for the given string as an artist
//         */
//        spotify.searchArtists(artistName, object : Callback<ArtistsPager> {
//            override fun success(t: ArtistsPager?, response: Response?) {
//                //TODO assign the pager to a list view or something
//            }
//
//            override fun failure(error: RetrofitError?) {
//                Log.e("findArtist", "bother")
//            }
//        })
//    }
//
//    fun getArtist(artistId: String) {
//        spotify.getArtist(artistId, object : Callback<Artist> {
//            override fun success(t: Artist?, response: Response?) {
//                //TODO set the centre of the map to be this artist
//                //TODO trigger search for related artists
//            }
//
//            override fun failure(error: RetrofitError?) {
//                //TODO empty the map? display an error message
//            }
//        })
//    }
}
