package me.nubuscu.spoofy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import me.nubuscu.spoofy.BuildConfig.SPOTIFY_ID

const val redirect_url = "spoofy://callback"
const val client_id = SPOTIFY_ID

class SpotifyAuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val builder = AuthenticationRequest.Builder(client_id, AuthenticationResponse.Type.TOKEN, redirect_url)
        val scopes = arrayOf("streaming")
        builder.setScopes(scopes)

        val request = builder.build()
        AuthenticationClient.openLoginInBrowser(this, request)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.data == null) {
            return
        }
        val uri = intent.data
        val response = AuthenticationResponse.fromUri(uri)

        when (response.type) {
            AuthenticationResponse.Type.TOKEN -> {
                Log.d("spotify", "successfully retrieved token")
            }
            AuthenticationResponse.Type.ERROR -> {
                Log.e("spotify", "failed to get token")
            }
            else -> {
                Log.e("spotify", "something else happened entirely?")
            }
        }
    }
}