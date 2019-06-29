package me.nubuscu.spoofy

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kaaes.spotify.webapi.android.models.Pager
import kaaes.spotify.webapi.android.models.PlaylistSimple
import me.nubuscu.spoofy.utils.DataManager
import me.nubuscu.spoofy.view.PlaylistAdapter
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

class RecommendationsFragment : Fragment() {
    private val spotify = DataManager.instance.spotify

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recommendations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlistList: RecyclerView = view.findViewById(R.id.playlistList)
        playlistList.layoutManager = LinearLayoutManager(activity)
        spotify.getMyPlaylists(object : Callback<Pager<PlaylistSimple>> {
            override fun success(pager: Pager<PlaylistSimple>?, response: Response?) {
                val playlists: List<PlaylistSimple> = pager?.items ?: listOf()
                playlistList.adapter = PlaylistAdapter(playlists) { selected ->
                    //TODO congrats
                }
            }

            override fun failure(error: RetrofitError?) {
                Log.e("spotify", "could not get user's playlists")
            }
        })

    }
}