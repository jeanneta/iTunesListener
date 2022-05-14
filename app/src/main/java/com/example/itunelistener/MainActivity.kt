package com.example.itunelistener

import android.app.ListActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.itunelistener.databinding.ActivitySwipeRefreshBinding

class MainActivity : AppCompatActivity(), iTunenRecycleViewAdapter.RecycleViewClickListener {

    val adapter: iTunenRecycleViewAdapter by lazy {
        iTunenRecycleViewAdapter(listOf<SongData>(), this)
}

    override fun onItemClick(view: View, position: Int) {
        Toast.makeText(this, adapter.songs[position].title, Toast.LENGTH_LONG).show()
        val intent = Intent(this, PreviewActivity::class.java)
        val song =  adapter.getItem(position)
        intent.putExtra("title", song.title)
        intent.putExtra("cover", song.cover)
        intent.putExtra("url", song.url)
        startActivity(intent)

    }


    private fun loadList(){
        iTuneSAX(object: ParserListener{
            override fun start() {
                binding.swipeRefreshLayout.isRefreshing = true
            }

            override fun finish(songs: List<SongData>) {

                adapter.songs = songs
                binding.swipeRefreshLayout.isRefreshing = false

            }

        }).parseURL("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=25/xml")
    }


lateinit var binding: ActivitySwipeRefreshBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_swipe_refresh)

        binding.recycleView.adapter = adapter
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.addItemDecoration(DividerItemDecoration(
            binding.recycleView.context,
            DividerItemDecoration.VERTICAL))

        // Refresh
        binding.swipeRefreshLayout.setOnRefreshListener {
            loadList()
        }
        loadList()
    }
}

