package me.nubuscu.spoofy.graph

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point

open class Node(x: Int = 0, y: Int = 0, val label: String, val canvas: Canvas) : Point(x, y) {
    private val shapePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.DKGRAY
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.BLACK
        textSize = 36F  // TODO find a way to get dimension resources without context
        textAlign = Paint.Align.CENTER
    }

    private val radius = 15.0F

    fun draw(scaleFactor: Float) {
        canvas.drawCircle(x.toFloat(), y.toFloat(), radius, shapePaint)
        textPaint.textSize = textPaint.textSize/scaleFactor
        canvas.drawText(label, x.toFloat(), y.toFloat() - radius, textPaint)
    }
}