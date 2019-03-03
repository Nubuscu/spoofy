package me.nubuscu.spoofy

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kaaes.spotify.webapi.android.SpotifyApi

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // https://developer.spotify.com/documentation/android/guides/android-authentication
        // add a login button to trigger the other activity

    }

    fun runLogin(view: View) {
        val intent = Intent(this, SpotifyAuthActivity::class.java)
        startActivity(intent)
    }
}
