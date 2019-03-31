package me.nubuscu.spoofy.graph

import android.graphics.Canvas
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import me.nubuscu.spoofy.utils.DataManager

/**
 * class to hold the neighbour generation separate from drawing logic
 */
class ArtistNode(val label: String, val canvas: Canvas, val artistId: String) {

    private var adjacentNodes: MutableSet<ArtistNode> = HashSet()
    private lateinit var node: Node

    /**
     * display the underlying visible node and draw edges to adjacent nodes
     * does not actually draw the other nodes, just the edge.
     */
    fun draw(x: Int, y: Int) {
        node = Node(x, y, label, canvas)
        node.draw()

    }

    /**
     * creates ArtistNodes for each related artist and tracks them in adjacentNodes
     * All adjacent nodes are added to the network.
     */
    suspend fun addAdjacentToNetwork(network: ArtistNetwork, recursionDepth: Int) {
        Log.d("FOO", "adding to network, depth = $recursionDepth")
        val spotify = DataManager.instance.spotify
        network.artists.add(this)
        if (recursionDepth > 0) {
            val artists = GlobalScope.async { spotify.getRelatedArtists(artistId)?.artists }.await()
            artists?.forEach { artist ->
                val newNode = ArtistNode(artist.name, canvas, artist.id)
                network.artists.add(newNode)
                adjacentNodes.add(newNode)
            }
            adjacentNodes.forEach { it.addAdjacentToNetwork(network, recursionDepth - 1) }
        }
    }

//    fun getAdjacentNodes() {
//        val spotify = DataManager.instance.spotify
//        spotify.getRelatedArtists(artistId, object : Callback<Artists> {
//            override fun success(artists: Artists?, response: Response?) {
//                artists?.artists?.let { artistList ->
//                    artistList.forEach { artist ->
//                        adjacentNodes.add(ArtistNode(artist.name, canvas, artist.id))
//                        Log.d("FOO", artist.name)
//                    }
//                }
//            }
//
//            override fun failure(error: RetrofitError?) {
//                Log.e("map", "failed to get related artists", error)
//                adjacentNodes = ArrayList()
//            }
//
//        })
//    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as ArtistNode

        if (other.artistId != this.artistId) return false

        return true
    }

    override fun hashCode(): Int {
        return artistId.hashCode()
    }
}