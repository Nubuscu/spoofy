package me.nubuscu.spoofy.viewmodel

import android.arch.lifecycle.ViewModel
import me.nubuscu.spoofy.graph.ArtistNetwork

class NetworkViewModel : ViewModel() {
    var model: ArtistNetwork? = null
}