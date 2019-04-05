package me.nubuscu.spoofy.graph

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import me.nubuscu.spoofy.viewmodel.NetworkViewModel

const val defaultRecursionDepth = 2

class GraphView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var artistId: String? = null
    private var artistName: String = ""
    private var mScaleFactor = 1f
    var mViewModel: NetworkViewModel? = null
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
            GlobalScope.async {
                requestUpdateMapAsync(defaultRecursionDepth).await()
                invalidate()
            }
        }

    }

    private val mScaleDetector = ScaleGestureDetector(context, scaleListener)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mScaleDetector.onTouchEvent(event)
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            save()
            scale(mScaleFactor, mScaleFactor)
            drawMap(canvas)
            restore()
        }
    }


    private fun requestUpdateMapAsync(numLayers: Int) = GlobalScope.async {
        mViewModel?.model = ArtistNetwork()
        mViewModel?.model!!.generateFromArtistAsync(artistName, artistId!!, numLayers).await()
    }

    private fun drawMap(canvas: Canvas) {
        if (artistId != null) {
            mViewModel?.model?.draw(canvas)
        }
    }


}