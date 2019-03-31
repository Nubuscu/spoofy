package me.nubuscu.spoofy.graph

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class GraphView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    var artistId: String? = null

    override fun onDraw(canvas: Canvas?) {
        Log.d("FOO", "onDraw called")
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
        Log.d("FOO", "generateMap called")
        if (artistId != null) {
            val network = ArtistNetwork(canvas)
            GlobalScope.launch {
                network.generateFromArtistAsync("asdf", artistId!!, 2).await()
                network.draw()
            }
            network.artists.forEach { Log.d("FOO", it.label) }
//            val test = Node(400, 400, "hello", canvas)
//            val test2 = Node(400, 700, "there", canvas)
//            val test3 = Edge(test, test2, canvas)
//            test.draw()
//            test2.draw()
//            test3.draw()
        }
    }


}