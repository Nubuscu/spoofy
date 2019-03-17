package me.nubuscu.spoofy.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kaaes.spotify.webapi.android.models.Artist
import me.nubuscu.spoofy.R


class ArtistAdapter(val artists: List<Artist>) :
    RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {

    class ArtistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val artistImage: ImageView = view.findViewById(R.id.artistImage)
        val artistNameText: TextView = view.findViewById(R.id.artistNameText)
    }

    override fun getItemCount(): Int = artists.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ArtistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.artist_view_holder, parent, false)
        return ArtistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, i: Int) {
        holder.artistNameText.text = artists[i].name
//        can't always find a bitmap image to use?
//        holder.artistImage.setImageURI(Uri.parse(artists[i].images[0].url))
    }
}