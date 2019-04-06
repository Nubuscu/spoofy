package me.nubuscu.spoofy.graph

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import me.nubuscu.spoofy.viewmodel.NetworkViewModel

const val defaultRecursionDepth = 2

class GraphView(context: Context, attrs: AttributeSet) : MovableView(context, attrs) {
    private var artistId: String? = null
    private var artistName: String = ""
    var mViewModel: NetworkViewModel? = null


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


    private fun requestUpdateMapAsync(numLayers: Int) = GlobalScope.async {
        mViewModel?.model = ArtistNetwork()
        mViewModel?.model!!.generateFromArtistAsync(artistName, artistId!!, numLayers).await()
    }

    override var onDrawFunction: (Canvas, Float) -> Unit = { canvas, scaleFactor ->
        mViewModel?.model?.draw(canvas, scaleFactor)
    }



}