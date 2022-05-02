package com.example.itunelistener

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.itunelistener.databinding.ItuneListItemBinding

class iTunenRecycleViewAdapter(data: List<SongData>, val listener: RecycleViewClickListener): RecyclerView.Adapter<iTunenRecycleViewAdapter.ViewHolder>() {
var songs: List<SongData> = data
    set(value){
        field=value
        notifyDataSetChanged()
    }
    interface RecycleViewClickListener{
        fun onItemClick(view: View, position: Int)
    }

    class ViewHolder(val binding: ItuneListItemBinding, val listener: RecycleViewClickListener): RecyclerView.ViewHolder(binding.root), View.OnClickListener{
//        val textview: TextView = view.findViewById(R.id.textView)
//        val imageView: ImageView = view.findViewById(R.id.imageView)

        override fun onClick(p0: View?) {
            listener.onItemClick(binding.root, absoluteAdapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.itune_list_item, parent, false)
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItuneListItemBinding = DataBindingUtil.inflate(
            inflater, R.layout.itune_list_item, parent, false)

        val holder = ViewHolder(binding, listener)
        binding.root.setOnClickListener(holder)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.imageView.setImageBitmap(songs[position].cover)
//        holder.textview.text = songs[position].title
        holder.binding.songData = songs[position]
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    fun getItem(position: Int): SongData{
        return songs[position]
    }
}