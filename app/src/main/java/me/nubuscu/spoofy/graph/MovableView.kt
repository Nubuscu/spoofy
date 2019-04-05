package me.nubuscu.spoofy.graph

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import me.nubuscu.spoofy.viewmodel.NetworkViewModel

open class MovableView(context: Context, attrs: AttributeSet): View(context, attrs) {

    open var onDrawFunction: (canvas: Canvas) -> Unit = {} // override this to set what gets drawn

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
            onDrawFunction(canvas)
            restore()
        }
    }
}