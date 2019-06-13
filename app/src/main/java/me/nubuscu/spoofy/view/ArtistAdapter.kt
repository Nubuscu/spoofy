package me.nubuscu.spoofy.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kaaes.spotify.webapi.android.models.Artist
import me.nubuscu.spoofy.R
import me.nubuscu.spoofy.utils.DownloadImageTask
import java.lang.ref.WeakReference


class ArtistAdapter(private val artists: List<Artist>) :
    RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {

    constructor(artists: List<Artist>, clickListener: (Artist) -> Unit) : this(artists) {
        this.clickListener = clickListener
    }

    private var clickListener = { _: Artist -> }
    class ArtistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val artistImage: ImageView = view.findViewById(R.id.artistImage)
        val artistNameText: TextView = view.findViewById(R.id.artistNameText)
    }

    override fun getItemCount(): Int = artists.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ArtistViewHolder {


        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.artist_view_holder, parent, false)
        val holder = ArtistViewHolder(view)
        view.setOnClickListener {
            clickListener(artists[holder.adapterPosition])
        }
        return holder
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, i: Int) {
        holder.artistNameText.text = artists[i].name
        val images = artists[i].images
        holder.artistImage.setImageBitmap(null)
        if (images.isNotEmpty()) {
            DownloadImageTask(WeakReference(holder.artistImage)).execute(images[0].url)
        }
    }
}

