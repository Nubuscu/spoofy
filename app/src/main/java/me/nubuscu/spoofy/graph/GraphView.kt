package me.nubuscu.spoofy.graph

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

const val defaultRecursionDepth = 1

class GraphView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var artistId: String? = null
    private var artistName: String = ""
    private var mScaleFactor = 1f
    private val scaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleFactor *= detector.scaleFactor

            mScaleFactor = Math.max(0.5f, Math.min(mScaleFactor, 3.0f))

            invalidate()
            return true
        }
    }

    fun updateCentreArtist(artistId: String, artistName: String) {
        if (this.artistId != artistId) {
            this.artistName = artistName
            this.artistId = artistId

        }

    }

    private val mScaleDetector = ScaleGestureDetector(context, scaleListener)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mScaleDetector.onTouchEvent(event)
        Log.d("debug", "this is a touch event")
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            save()
            scale(mScaleFactor, mScaleFactor)

            // needs to be blocking so the canvas doesn't get destroyed before I can draw on it
            runBlocking {
                //TODO show a spinny thing to indicate it's loading
                generateMapAsync(canvas, defaultRecursionDepth).await()
            }
            restore()
        }
    }

    private lateinit var network: ArtistNetwork

    private fun generateMapAsync(canvas: Canvas, numLayers: Int) = GlobalScope.async {
        if (artistId != null) {
            network = ArtistNetwork(canvas)
            network.generateFromArtistAsync(artistName, artistId!!, numLayers).await()
            network.draw()
//            network.artists.forEach { Log.d("FOO", it.label) }
        }
    }


}