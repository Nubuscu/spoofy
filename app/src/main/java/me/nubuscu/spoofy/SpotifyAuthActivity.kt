package me.nubuscu.spoofy

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import me.nubuscu.spoofy.BuildConfig.SPOTIFY_ID

const val request_code = 1337
const val redirect_url = "spoofy://callback"
const val client_id = SPOTIFY_ID


class SpotifyAuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val builder = AuthenticationRequest.Builder(client_id, AuthenticationResponse.Type.TOKEN, redirect_url)
        val scopes = arrayOf("user-library-read", "playlist-read-private", "user-read-recently-played", "user-top-read")
        builder.setScopes(scopes)

        val request = builder.build()
        AuthenticationClient.openLoginActivity(this, request_code, request)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == request_code) {
            val response = AuthenticationClient.getResponse(resultCode, data)
            when (response.type) {
                AuthenticationResponse.Type.TOKEN -> {
                    Log.d("auth", "successfully retrieved token")
                    val openMainIntent = Intent(this, MainActivity::class.java)
                    openMainIntent.putExtra("spotifyToken", response.accessToken)
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    startActivity(openMainIntent)
                }
                AuthenticationResponse.Type.ERROR -> {
                    Log.e("auth", "failed to get token")
                    Toast.makeText(this, "Failed to log in. Please try again", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Log.e("auth", "something else happened entirely?")
                    Toast.makeText(this, "An error occurred ${response.type.name}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}