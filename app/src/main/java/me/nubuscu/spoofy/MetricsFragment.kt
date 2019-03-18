package me.nubuscu.spoofy

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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
import me.nubuscu.spoofy.viewmodel.MetricsViewModel
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

class MetricsFragment : Fragment() {

    private lateinit var profileNameTextView: TextView
    private lateinit var metricsRecyclerView: RecyclerView
    private lateinit var metricsViewModel: MetricsViewModel
    private val spotify = DataManager.instance.spotify

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
        metricsViewModel = ViewModelProviders.of(this).get(MetricsViewModel::class.java)
        metricsViewModel.topArtists.observe(this, Observer { newArtists ->
            if (newArtists != null) {
                metricsRecyclerView.adapter = ArtistAdapter(newArtists)
                (metricsRecyclerView.adapter as ArtistAdapter).notifyDataSetChanged()
            }
        })
        metricsViewModel.topSongs.observe(this, Observer { newSongs ->
            if (newSongs != null) {
                metricsRecyclerView.adapter = SongAdapter(newSongs)
                (metricsRecyclerView.adapter as SongAdapter).notifyDataSetChanged()
            }
        })

        val objectTypeSpinner = view.findViewById<Spinner>(R.id.objectTypeSpinner)
        val timeRangeSpinner = view.findViewById<Spinner>(R.id.timeRangeSpinner)

        objectTypeSpinner.adapter = ArrayAdapter<ObjectType>(
            context!!,
            android.R.layout.simple_spinner_item,
            ObjectType.values()
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        timeRangeSpinner.adapter = ArrayAdapter<TimeRange>(
            context!!,
            android.R.layout.simple_spinner_item,
            TimeRange.values()
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        val refreshButton = view.findViewById<Button>(R.id.refreshButton)
        refreshButton.setOnClickListener {
            populateTopStats(
                spotify,
                timeRangeSpinner.selectedItem as TimeRange,
                objectTypeSpinner.selectedItem as ObjectType
            )
        }
    }

    override fun onStart() {
        super.onStart()
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
                    metricsViewModel.topArtists.postValue(list)
                }

                override fun failure(error: RetrofitError?) {
                    Log.d("metrics", "topStats failure", error)
                }
            })
            ObjectType.SONGS -> spotify.getTopTracks(params, object : Callback<Pager<Track>> {
                override fun success(trackPager: Pager<Track>?, response: Response?) {
                    val list = trackPager?.items?.toList() ?: listOf()
                    metricsViewModel.topSongs.postValue(list)
                }

                override fun failure(error: RetrofitError?) {
                    Log.d("metrics", "topStats failure", error)
                }

            })
        }

    }
}