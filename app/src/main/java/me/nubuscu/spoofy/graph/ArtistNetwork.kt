package me.nubuscu.spoofy.graph

import android.graphics.Canvas
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class ArtistNetwork(private val canvas: Canvas) {

    val artists: MutableSet<ArtistNode> = HashSet()

    fun generateFromArtistAsync(artistName: String, artistId: String, numLayers: Int) = GlobalScope.async{
        Log.d("FOO", "generateFromArtist called")
        val artistNode = ArtistNode(artistName, canvas, artistId)
        artistNode.addAdjacentToNetwork(this@ArtistNetwork, numLayers)
    }

    fun draw() {
        for (node in artists) {
            node.draw(400, 400)
        }
        TODO("implement me pls")
    }
}