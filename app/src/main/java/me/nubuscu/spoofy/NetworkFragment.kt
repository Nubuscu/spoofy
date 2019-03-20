package me.nubuscu.spoofy

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class NetworkFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_network, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val openSearchButton = view.findViewById<Button>(R.id.openSearchButton)
        openSearchButton.setOnClickListener {
            Log.d("asdf", (this.activity != null).toString())
            val openSearchIntent = Intent(this.activity, ArtistSearchActivity::class.java)
            startActivity(openSearchIntent)
        }
    }
}