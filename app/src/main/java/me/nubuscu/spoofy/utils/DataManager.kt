package me.nubuscu.spoofy.utils

import android.content.Context
import kaaes.spotify.webapi.android.SpotifyService
import java.lang.ref.WeakReference

/**
 * 'singleton' class to share data between fragments, namely a spotify client
 */
class DataManager {

    companion object {
        val instance = DataManager()
    }

    lateinit var spotify: SpotifyService
    lateinit var context: WeakReference<Context>
}