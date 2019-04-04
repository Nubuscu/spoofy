package me.nubuscu.spoofy.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kaaes.spotify.webapi.android.models.Artist
import kaaes.spotify.webapi.android.models.Track
import me.nubuscu.spoofy.enums.ObjectType
import me.nubuscu.spoofy.enums.TimeRange

class MetricsViewModel: ViewModel() {
    val topArtists: MutableLiveData<List<Artist>> by lazy {
        MutableLiveData<List<Artist>>()
    }

    val topSongs: MutableLiveData<List<Track>> by lazy {
        MutableLiveData<List<Track>>()
    }

    var selectedObjType: ObjectType = ObjectType.SONGS

    var selectedTimeRange: TimeRange = TimeRange.LONG_TERM

}