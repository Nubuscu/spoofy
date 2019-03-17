package me.nubuscu.spoofy.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kaaes.spotify.webapi.android.models.Artist
import kaaes.spotify.webapi.android.models.Track

class MetricsViewModel: ViewModel() {
    val topArtists: MutableLiveData<List<Artist>> by lazy {
        MutableLiveData<List<Artist>>()
    }

    val topSongs: MutableLiveData<List<Track>> by lazy {
        MutableLiveData<List<Track>>()
    }

}