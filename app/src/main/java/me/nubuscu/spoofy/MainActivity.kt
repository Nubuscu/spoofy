package me.nubuscu.spoofy

import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import kaaes.spotify.webapi.android.SpotifyApi
import me.nubuscu.spoofy.utils.DataManager
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {
    companion object {
        var selectedFragment: Class<*> = MetricsFragment::class.java
    }
    //    spotify deps
    private lateinit var token: String
    //    nav bar things
    private lateinit var mDrawer: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var nvDrawer: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //setup spotify client
        token = intent.getStringExtra("spotifyToken")
        val api = SpotifyApi()
        api.setAccessToken(token)
        DataManager.instance.spotify = api.service
        DataManager.instance.context = WeakReference(this)

        toolbar = findViewById(R.id.toolbar)
        mDrawer = findViewById(R.id.drawer_layout)
        nvDrawer = findViewById(R.id.nvView)
        setSupportActionBar(toolbar)
        toggle = ActionBarDrawerToggle(
            this,
            mDrawer,
            toolbar,
            R.string.open_nav_drawer_desc,
            R.string.close_nav_drawer_desc
        )
        mDrawer.addDrawerListener(toggle)

        setupDrawerContent(nvDrawer)
        openFragment(selectedFragment.newInstance() as Fragment)

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
        selectedFragment = when (item!!.itemId) {
            R.id.nav_metrics_fragment -> MetricsFragment::class.java
            R.id.nav_discover_map -> NetworkFragment::class.java
            R.id.nav_recommendations_fragment -> RecommendationsFragment::class.java
            else -> selectedFragment // do nothing if confused
        }

        openFragment(selectedFragment.newInstance() as Fragment)

        item.isChecked = true
        mDrawer.closeDrawers()
    }

    private fun openFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit()

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

}
