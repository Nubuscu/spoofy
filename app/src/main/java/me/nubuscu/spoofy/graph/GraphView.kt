package me.nubuscu.spoofy.graph

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

const val defaultRecursionDepth = 1

class GraphView(context: Context, attrs: AttributeSet) : MovableView(context, attrs) {
    private var artistId: String? = null
    private var artistName: String = ""


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

    override var onDrawFunction = { canvas: Canvas ->
        if (artistId != null) {
            mViewModel?.model?.draw(canvas)
        }
    }



}