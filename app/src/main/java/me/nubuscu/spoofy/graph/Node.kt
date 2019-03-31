package me.nubuscu.spoofy.graph

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import me.nubuscu.spoofy.R
import java.lang.ref.WeakReference

open class Node(x: Int = 0, y: Int = 0, val label: String, val canvas: Canvas) : Point(x, y) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.BLACK
        textSize = 48F  // TODO find a way to get dimension resources without context
    }
    private val radius = 15.0F

    fun draw() {
        canvas.drawCircle(x.toFloat(), y.toFloat(), radius, paint)
        canvas.drawText(label, x.toFloat(), y.toFloat(), paint)
    }
}