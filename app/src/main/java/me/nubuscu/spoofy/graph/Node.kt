package me.nubuscu.spoofy.graph

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point

class Node(x: Int, y: Int, val label: String, val canvas: Canvas) : Point(x, y) {
    var adjacentNodes: MutableCollection<Node> = ArrayList()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.BLACK
    }
    private val radius = 15.0F

    fun draw() {
        drawSelf()
        drawAdjacentEdges()
    }
    private fun drawSelf() {
        canvas.drawCircle(x.toFloat(), y.toFloat(), radius, paint)
        canvas.drawText(label, x.toFloat(), y.toFloat(), paint)
    }

    private fun drawAdjacentEdges() {
        for (node in adjacentNodes) {
            Edge(this, node, canvas).draw()
        }
    }
}