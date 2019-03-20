package me.nubuscu.spoofy.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kaaes.spotify.webapi.android.models.Track
import me.nubuscu.spoofy.R

class SongAdapter(val songs: List<Track>) :
    RecyclerView.Adapter<SongAdapter.SongViewHolder>() {
    class SongViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val songTitle: TextView = view.findViewById(R.id.songNameText)
        val songArtist: TextView = view.findViewById(R.id.songArtistText)
    }

    override fun getItemCount(): Int = songs.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.song_view_holder, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, i: Int) {
        holder.songTitle.text = songs[i].name
        holder.songArtist.text = songs[i].artists.map { artist -> artist.name }.toString()
    }
}