package me.nubuscu.spoofy

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import me.nubuscu.spoofy.graph.GraphView

public const val PICK_ARTIST_REQUEST = 42

class NetworkFragment : Fragment() {
    lateinit var graphView: GraphView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_network, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val openSearchButton: Button = view.findViewById(R.id.openSearchButton)
        openSearchButton.setOnClickListener {
            val openSearchIntent = Intent(this.activity, ArtistSearchActivity::class.java)
            startActivityForResult(openSearchIntent, PICK_ARTIST_REQUEST)
        }

        graphView = view.findViewById(R.id.graphView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_ARTIST_REQUEST && resultCode == Activity.RESULT_OK) {
            Log.d("FOO", data.toString())
            data?.let {
                generateNetwork(data.getStringExtra("artistId"))
            }
        }
    }

    fun generateNetwork(artistId: String) {
        Log.d("FOO", "generateNetwork called")
        graphView.artistId = artistId
        graphView.invalidate()
    }
}