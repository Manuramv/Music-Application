package com.thales.musicsdk.musicsdk.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thales.musicapplication.R
import com.thales.musicapplication.listners.SongClickListner
import com.thales.musicsdk.musicsdk.models.Song
import com.thales.musicsdk.musicsdk.utils.FileUtils.getFileName


class SongAdapter(val songClickListner: SongClickListner) : RecyclerView.Adapter<ViewHolder>() {

    private val items = ArrayList<Song>()

    fun setItems(items: ArrayList<Song>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false) as View
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(items[position], songClickListner,position)
    }
}

class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    val txtSongName = itemView.findViewById<TextView>(R.id.txtSongName)
    val txtArtistName = itemView.findViewById<TextView>(R.id.txtArtistName)

    fun bind(
        item: Song,
        songClickListner: SongClickListner,
        position: Int
    ) {
        txtSongName.text = getFileName(item.name)
        txtArtistName.text = item.filePath

        itemView.setOnClickListener({
            songClickListner.onSongSelected(position)
        })
    }

}

