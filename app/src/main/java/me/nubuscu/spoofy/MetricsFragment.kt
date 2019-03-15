package me.nubuscu.spoofy

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import kaaes.spotify.webapi.android.SpotifyService
import kaaes.spotify.webapi.android.models.Artist
import kaaes.spotify.webapi.android.models.Pager
import kaaes.spotify.webapi.android.models.Track
import kaaes.spotify.webapi.android.models.UserPrivate
import me.nubuscu.spoofy.view.ArtistAdapter
import me.nubuscu.spoofy.view.SongAdapter
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

class MetricsFragment : Fragment() {

    private lateinit var profileNameTextView: TextView
    private lateinit var metricsRecyclerView: RecyclerView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_metrics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewManager = LinearLayoutManager(this.context)
        profileNameTextView = view.findViewById(R.id.profileNameTextView)
        metricsRecyclerView = view.findViewById(R.id.metricsRecyclerView)
        metricsRecyclerView.setHasFixedSize(true)
        metricsRecyclerView.layoutManager = viewManager

        val objectTypeSpinner = view.findViewById<Spinner>(R.id.objectTypeSpinner)
        val timeRangeSpinner = view.findViewById<Spinner>(R.id.timeRangeSpinner)
        val refreshButton = view.findViewById<Button>(R.id.refreshButton)
        refreshButton.setOnClickListener {
            //            populateTopStats()
        }
    }

    override fun onStart() {
        super.onStart()
        val spotify = DataManager.instance.spotify
        populateUserInfo(spotify)
        populateTopStats(spotify, TimeRange.LONG_TERM, ObjectType.ARTISTS)
    }

    fun populateUserInfo(spotify: SpotifyService) {
        spotify.getMe(object : Callback<UserPrivate> {
            override fun success(t: UserPrivate?, response: Response?) {
                profileNameTextView.text = t?.display_name
            }

            override fun failure(error: RetrofitError?) {
                Log.d("metrics", "oops?")
            }
        })
    }

    fun populateTopStats(spotify: SpotifyService, timeRange: TimeRange, type: ObjectType) {
        val params: MutableMap<String, Any> = mutableMapOf("time_range" to timeRange.value)

        when (type) {

            ObjectType.ARTISTS -> spotify.getTopArtists(params, object : Callback<Pager<Artist>> {
                override fun success(artistPager: Pager<Artist>?, response: Response?) {
                    val list = artistPager?.items?.toList() ?: listOf()
                    metricsRecyclerView.adapter = ArtistAdapter(list)
                    (metricsRecyclerView.adapter as ArtistAdapter).notifyDataSetChanged()
                }

                override fun failure(error: RetrofitError?) {
                    Log.d("metrics", "topStats failure", error)
                }
            })
            ObjectType.SONGS -> spotify.getTopTracks(params, object : Callback<Pager<Track>> {
                override fun success(trackPager: Pager<Track>?, response: Response?) {
                    val list = trackPager?.items?.toList() ?: listOf()
                    metricsRecyclerView.adapter = SongAdapter(list)
                    (metricsRecyclerView.adapter as SongAdapter).notifyDataSetChanged()
                }

                override fun failure(error: RetrofitError?) {
                    Log.d("metrics", "topStats failure", error)
                }

            })
        }

    }
}