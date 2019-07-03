package me.nubuscu.spoofy.graph

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import me.nubuscu.spoofy.R
import me.nubuscu.spoofy.utils.DataManager

class Edge(private val start: Node, private val end: Node, private val canvas: Canvas) : Path() {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = DataManager.instance.context.get()?.getColor(R.color.purple_dark) ?: Color.BLACK
        strokeWidth = 5.0F
    }

    fun draw() {
        this.apply {
            reset()
            moveTo(start.x.toFloat(), start.y.toFloat())
            lineTo(end.x.toFloat(), end.y.toFloat())
            canvas.drawPath(this, paint)
        }
    }
}