package me.nubuscu.spoofy.graph

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

class GraphView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    var artistId: String? = null

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            generateMap(canvas)
        }

    }

//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        //TODO figure out the location of the touch event and find what node has been tapped
//        return super.onTouchEvent(event)
//    }

    private fun generateMap(canvas: Canvas) {
        if (artistId != null) {
            val test = Node(400, 400, "hello", canvas)
            val test2 = Node(400, 700, "there", canvas)
            val test3 = Edge(test, test2, canvas)
            test.draw()
            test2.draw()
            test3.draw()
        }
    }


}