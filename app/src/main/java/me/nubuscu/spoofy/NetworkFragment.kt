package me.nubuscu.spoofy

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import me.nubuscu.spoofy.graph.GraphView
import me.nubuscu.spoofy.viewmodel.NetworkViewModel

public const val PICK_ARTIST_REQUEST = 42

class NetworkFragment : Fragment() {
    lateinit var graphView: GraphView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_network, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val openSearchButton: FloatingActionButton = view.findViewById(R.id.openSearchButton)
        openSearchButton.setOnClickListener {
            val openSearchIntent = Intent(this.activity, ArtistSearchActivity::class.java)
            startActivityForResult(openSearchIntent, PICK_ARTIST_REQUEST)
        }

        graphView = view.findViewById(R.id.graphView)
        graphView.mViewModel = ViewModelProviders.of(activity!!).get(NetworkViewModel::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_ARTIST_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.let {
                val loadingBar: ProgressBar? = view?.findViewById(R.id.loadingBar)
                loadingBar?.let { loadingBar.visibility = View.VISIBLE }
                generateNetwork(data.getStringExtra("artistId"), data.getStringExtra("artistName"))
                loadingBar?.let { loadingBar.visibility = View.GONE }
            }
        }
    }

    private fun generateNetwork(artistId: String, artistName: String) {
        graphView.apply {
            updateCentreArtist(artistId, artistName)
            invalidate()

        }
    }
}