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
import me.nubuscu.spoofy.view.EndlessRecyclerViewScrollListener
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
        val layoutManager = LinearLayoutManager(activity)
        val playlistAdapter = PlaylistAdapter(mutableListOf()) { selected ->
            //TODO congrats
        }
        val playlistsCallback = object : Callback<Pager<PlaylistSimple>> {
            override fun success(pager: Pager<PlaylistSimple>?, response: Response?) {
                val playlists: MutableList<PlaylistSimple> = pager?.items ?: mutableListOf()
                playlistAdapter.playlists.addAll(playlists)
                playlistAdapter.notifyItemRangeChanged(playlistAdapter.itemCount, playlists.size)
            }

            override fun failure(error: RetrofitError?) {
                Log.e("spotify", "could not get user's playlists")
            }
        }
        val scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemCount: Int, view: RecyclerView) {
                spotify.getMyPlaylists(
                    mapOf(Pair("offset", playlistAdapter.itemCount)), playlistsCallback
                )
            }
        }
        playlistList.addOnScrollListener(scrollListener)
        playlistList.layoutManager = layoutManager
        playlistList.adapter = playlistAdapter
        spotify.getMyPlaylists(playlistsCallback)
    }
}