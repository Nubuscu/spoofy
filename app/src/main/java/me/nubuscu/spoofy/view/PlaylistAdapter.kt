package me.nubuscu.spoofy.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kaaes.spotify.webapi.android.models.PlaylistSimple
import me.nubuscu.spoofy.R
import me.nubuscu.spoofy.utils.DownloadImageTask
import java.lang.ref.WeakReference

class PlaylistAdapter(
    private val playlists: List<PlaylistSimple>,
    private val clickListener: (PlaylistSimple) -> Unit
) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {
    class PlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.playlistImageView)
        val title: TextView = view.findViewById(R.id.playlistTitleTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): PlaylistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.playlist_view_holder, parent, false)
        val holder = PlaylistViewHolder(view)
        view.setOnClickListener {
            clickListener(playlists[holder.adapterPosition])
        }
        return holder
    }

    override fun getItemCount() = playlists.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, i: Int) {
        holder.image.setImageBitmap(null)
        DownloadImageTask(WeakReference(holder.image)).execute(
            playlists[i].images[0].url
        )
        holder.title.text = playlists[i].name
    }


}