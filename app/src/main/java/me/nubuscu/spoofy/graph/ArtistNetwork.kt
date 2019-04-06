package me.nubuscu.spoofy.graph

import android.graphics.Canvas
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class ArtistNetwork() {

    val artists: MutableSet<ArtistNode> = HashSet()
    var centralNode: ArtistNode? = null

    fun generateFromArtistAsync(artistName: String, artistId: String, numLayers: Int) = GlobalScope.async {
        if (centralNode?.artistId == artistId) {
            return@async
        } else {
            Log.d("debug", "running adjacency request thing")
            centralNode = ArtistNode(artistName, artistId)
            centralNode!!.addAdjacentToNetwork(this@ArtistNetwork, numLayers)
        }
    }

    fun draw(canvas: Canvas, scaleFactor: Float) {
        val centreX = canvas.width / 2
        val centreY = canvas.height / 2
        centralNode!!.draw(canvas, centreX, centreY, scaleFactor)
        // TODO generate a series of co-ordinates to draw other nodes at
        for (x in artists zip makeCoOrds(centreX, centreY, artists.size, 300)) {
            x.first.draw(canvas, x.second.first, x.second.second, scaleFactor)
        }
//        artists.forEachIndexed { i, a ->
//            a.draw(400, 400)
//        }
    }

    /**
     * creates a list of pairs of x/y co-ordinates in a circle around a central x/y point
     * numPoints determines the length of the list, radius determines the size of the circle
     */
    private fun makeCoOrds(baseX: Int, baseY: Int, numPoints: Int, radius: Int): List<Pair<Int, Int>> {
        val points: MutableList<Pair<Int, Int>> = ArrayList()
        for (i in 0..numPoints) {
            points.add(makeCoOrd(baseX, baseY, i, numPoints, radius))
        }
        return points
    }

    private fun makeCoOrd(baseX: Int, baseY: Int, i: Int, numPoints: Int, radius: Int): Pair<Int, Int> {
        val x = baseX + radius * Math.cos(2 * Math.PI * i / numPoints)
        val y = baseY + radius * Math.sin(2 * Math.PI * i / numPoints)
        return Pair(x.toInt(), y.toInt())

    }
}