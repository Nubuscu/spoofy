package me.nubuscu.spoofy

import kaaes.spotify.webapi.android.SpotifyService

class ClientManager {
    companion object {
        val instance = ClientManager()
    }

    public lateinit var spotify: SpotifyService

}