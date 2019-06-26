package me.nubuscu.spoofy.recommendations

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kaaes.spotify.webapi.android.models.Recommendations
import me.nubuscu.spoofy.ArtistSearchActivity
import me.nubuscu.spoofy.PICK_ARTIST_REQUEST
import me.nubuscu.spoofy.R
import me.nubuscu.spoofy.utils.DataManager
import me.nubuscu.spoofy.utils.TitledFragment
import me.nubuscu.spoofy.view.SongAdapter
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response


class ArtistRecFragment : TitledFragment() {
    override val title: String
        get() = DataManager.instance.context.get()?.getString(R.string.artist_rec) ?: "Artist"

    private lateinit var recommendedList: RecyclerView
    private lateinit var selectedArtistTextView: TextView
    private val spotify = DataManager.instance.spotify

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_artist_rec, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recommendedList = view.findViewById(R.id.artistBasedRecList)
        recommendedList.layoutManager = LinearLayoutManager(context)
        selectedArtistTextView = view.findViewById(R.id.selectedArtistTextView)
        val selectArtistButton: Button = view.findViewById(R.id.selectArtistButton)
        selectArtistButton.setOnClickListener {
            val openSearchIntent = Intent(this.activity, ArtistSearchActivity::class.java)
            startActivityForResult(openSearchIntent, PICK_ARTIST_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_ARTIST_REQUEST && resultCode == Activity.RESULT_OK) {
            selectedArtistTextView.text = data.getStringExtra("artistName")
            getRecommendations(data.getStringExtra("artistId"))
        }
    }

    private fun getRecommendations(artistId: String) {
        spotify.getRecommendations(
            mapOf(Pair("seed_artists", artistId)), object : Callback<Recommendations> {
                override fun success(recommendations: Recommendations?, response: Response?) {
                    recommendedList.adapter = SongAdapter(recommendations?.tracks ?: listOf())
                }

                override fun failure(error: RetrofitError?) {
                    Toast.makeText(context, "could not get recommendations", Toast.LENGTH_SHORT).show()
                }

            })
    }
}