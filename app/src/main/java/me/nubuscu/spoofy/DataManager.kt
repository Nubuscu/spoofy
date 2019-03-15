package me.nubuscu.spoofy

import kaaes.spotify.webapi.android.SpotifyService

/**
 * 'singleton' class to share data between fragments, namely a spotify client
 */
class DataManager {

    companion object {
        val instance = DataManager()
    }

    lateinit var spotify: SpotifyService

}