package me.nubuscu.spoofy.graph

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import me.nubuscu.spoofy.R
import me.nubuscu.spoofy.utils.DataManager

open class Node(x: Int = 0, y: Int = 0, val label: String, val canvas: Canvas) : Point(x, y) {
    private val resources = DataManager.instance.context.get()?.resources
    private val shapePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = DataManager.instance.context.get()?.getColor(R.color.purple) ?: Color.BLACK
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = DataManager.instance.context.get()?.getColor(R.color.spotifyWhite) ?: Color.BLACK
        textSize = resources?.getDimension(R.dimen.graphFontSize) ?: 36f
        textAlign = Paint.Align.CENTER
    }

    private val radius = resources?.getDimension(R.dimen.nodeDotRadius) ?: 15.0F

    fun draw(scaleFactor: Float) {
        canvas.drawCircle(x.toFloat(), y.toFloat(), radius, shapePaint)
        textPaint.textSize = textPaint.textSize/scaleFactor
        canvas.drawText(label, x.toFloat(), y.toFloat() - radius, textPaint)
    }
}