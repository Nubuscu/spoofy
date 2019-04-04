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
import android.widget.*
import kaaes.spotify.webapi.android.SpotifyService
import kaaes.spotify.webapi.android.models.Artist
import kaaes.spotify.webapi.android.models.Pager
import kaaes.spotify.webapi.android.models.Track
import kaaes.spotify.webapi.android.models.UserPrivate
import me.nubuscu.spoofy.enums.ObjectType
import me.nubuscu.spoofy.enums.TimeRange
import me.nubuscu.spoofy.utils.DataManager
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        metricsViewModel = ViewModelProviders.of(activity!!).get(MetricsViewModel::class.java)
    }

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
        setupControls(view)
    }

    private fun setupControls(view: View) {
        val objectTypeToggleButton = view.findViewById<ToggleButton>(R.id.objTypeToggle).apply {
            isChecked = (metricsViewModel.selectedObjType == ObjectType.ARTISTS)
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    metricsViewModel.selectedObjType = ObjectType.ARTISTS

                } else {
                    metricsViewModel.selectedObjType = ObjectType.SONGS
                }
            }
        }

        val timeRangeSpinner = view.findViewById<Spinner>(R.id.timeRangeSpinner).apply {
            adapter = ArrayAdapter<TimeRange>(
                context!!,
                android.R.layout.simple_spinner_item,
                TimeRange.values()
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedItem = parent?.getItemAtPosition(position) as TimeRange
                    metricsViewModel.selectedTimeRange = selectedItem
                }
            }
            setSelection((adapter as ArrayAdapter<TimeRange>).getPosition(metricsViewModel.selectedTimeRange))
        }

        val refreshButton = view.findViewById<Button>(R.id.refreshButton)
        refreshButton.setOnClickListener {
            populateTopStats(
                spotify,
                metricsViewModel.selectedTimeRange,
                metricsViewModel.selectedObjType
            )
        }

    }

    override fun onStart() {
        super.onStart()
        populateUserInfo(spotify)
        populateTopStats(spotify, metricsViewModel.selectedTimeRange, metricsViewModel.selectedObjType)
    }

    private fun populateUserInfo(spotify: SpotifyService) {
        spotify.getMe(object : Callback<UserPrivate> {
            override fun success(t: UserPrivate?, response: Response?) {
                profileNameTextView.text = t?.display_name
            }

            override fun failure(error: RetrofitError?) {
                Log.d("metrics", "oops?")
            }
        })
    }

    private fun populateTopStats(spotify: SpotifyService, timeRange: TimeRange, type: ObjectType) {
        val params: MutableMap<String, Any> = mutableMapOf("time_range" to timeRange.value, "limit" to 50)

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