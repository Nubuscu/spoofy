package me.nubuscu.spoofy.graph

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import kotlin.math.max
import kotlin.math.min

open class MovableView(context: Context, attrs: AttributeSet): View(context, attrs) {

    open var onDrawFunction: (canvas: Canvas, scaleFactor: Float) -> Unit = {_, _ -> } // override this to set what gets drawn

    private var mLastTouchX = 0f
    private var mLastTouchY = 0f
    private var scalePointX = 0f
    private var scalePointY = 0f
    private var mPosX = 0f
    private var mPosY = 0f
    private var mScaleFactor = 1f
    private val scaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleFactor *= detector.scaleFactor

            mScaleFactor = max(0.5f, min(mScaleFactor, 3.0f))

            invalidate()
            return true
        }
    }

    private val mScaleDetector = ScaleGestureDetector(context, scaleListener)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mScaleDetector.onTouchEvent(event)
        val action = event.action
        when (action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                mLastTouchX = (event.x - scalePointX) / mScaleFactor
                mLastTouchY = (event.y - scalePointY) / mScaleFactor
            }
            MotionEvent.ACTION_MOVE -> {
                val x = (event.x - scalePointX) / mScaleFactor
                val y = (event.y - scalePointY) / mScaleFactor
                if (!mScaleDetector.isInProgress) {
                    val dx = x - mLastTouchX
                    val dy = y - mLastTouchY
                    mPosX += dx
                    mPosY += dy
                    invalidate()
                }
                mLastTouchX = x
                mLastTouchY = y
            }
            MotionEvent.ACTION_UP -> {
                mLastTouchX = 0f
                mLastTouchY = 0f
                invalidate()
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            save()
            translate(mPosX, mPosY)
            scale(mScaleFactor, mScaleFactor, width / 2f, height / 2f)
            onDrawFunction(canvas, mScaleFactor)
            restore()
        }
    }
}