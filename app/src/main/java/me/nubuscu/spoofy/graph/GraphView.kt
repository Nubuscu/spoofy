package me.nubuscu.spoofy.graph

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

const val defaultRecursionDepth = 1

class GraphView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    var artistId: String? = null
    var artistName: String = ""

    override fun onDraw(canvas: Canvas?) {
        Log.d("FOO", "onDraw called")
        super.onDraw(canvas)
        if (canvas != null) {
            // needs to be blocking so the canvas doesn't get destroyed before I can draw on it
            runBlocking {
                //TODO show a spinny thing to indicate it's loading
                generateMapAsync(canvas, defaultRecursionDepth).await()
            }
        }
    }

//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        //TODO figure out the location of the touch event and find what node has been tapped
//        return super.onTouchEvent(event)
//    }

    private fun generateMapAsync(canvas: Canvas, numLayers: Int) = GlobalScope.async {
        Log.d("FOO", "generateMapAsync called")
        if (artistId != null) {
            val network = ArtistNetwork(canvas)
            network.generateFromArtistAsync(artistName, artistId!!, numLayers).await()
            network.draw()
//            network.artists.forEach { Log.d("FOO", it.label) }
        }
    }


}