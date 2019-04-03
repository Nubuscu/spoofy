package me.nubuscu.spoofy

import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import kaaes.spotify.webapi.android.SpotifyApi
import me.nubuscu.spoofy.utils.DataManager
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {
    //    spotify deps
    private lateinit var token: String
    //    nav bar things
    private lateinit var mDrawer: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var nvDrawer: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //setup spotify client
        token = intent.getStringExtra("spotifyToken")
        val api = SpotifyApi()
        api.setAccessToken(token)
        DataManager.instance.spotify = api.service
        DataManager.instance.context = WeakReference(this)
        Log.d("FOO", "why are you like this?")

        //setup toolbar but only if it exists
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            toolbar = findViewById(R.id.toolbar)
            setSupportActionBar(toolbar)
            mDrawer = findViewById(R.id.drawer_layout)
            nvDrawer = findViewById(R.id.nvView)

            setupDrawerContent(nvDrawer)
            openFragment(MetricsFragment::class.java.newInstance() as Fragment)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item == null) {
            return super.onOptionsItemSelected(item)
        }
        when (item.itemId) {
            android.R.id.home -> {
                mDrawer.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupDrawerContent(navView: NavigationView) {
        navView.setNavigationItemSelectedListener { menuItem ->
            selectDrawerItem(menuItem)
            true
        }
    }

    private fun selectDrawerItem(item: MenuItem?) {
        //default to metrics page
        var fragmentClass: Class<*> = MetricsFragment::class.java
        when (item!!.itemId) {
            R.id.nav_metrics_fragment -> fragmentClass = MetricsFragment::class.java
            R.id.nav_discover_map -> fragmentClass = NetworkFragment::class.java
        }
        val fragment = fragmentClass.newInstance() as Fragment
        openFragment(fragment)

        item.isChecked = true
        mDrawer.closeDrawers()
    }

    private fun openFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit()

    }
}
