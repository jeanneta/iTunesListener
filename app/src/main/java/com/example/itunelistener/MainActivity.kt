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
//    val titles = mutableListOf<String>()
//    val adapter:ArrayAdapter<String> by lazy {
//        ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles)
//    }

//    val adapter:iTunesListViewAdapter by lazy {
//        iTunesListViewAdapter(this)
//    }
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

    val swipeRefreshLayout by lazy {
        findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
    }

    private fun loadList(){
        iTuneSAX(object: ParserListener{
            override fun start() {
                swipeRefreshLayout.isRefreshing = true
            }

            override fun finish(songs: List<SongData>) {
//                for(song in songs){
//                    titles.add(song.title)
////                    val textView = TextView(this@MainActivity)
////                    textView.text = song.title
////                    linearLayout.addView(textView)
//                }
//                adapter.notifyDataSetChanged()
                adapter.songs = songs
                swipeRefreshLayout.isRefreshing = false

            }

        }).parseURL("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=25/xml")
    }

//    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
//        super.onListItemClick(l, v, position, id)
////        Toast.makeText(this, songs[position], Toast.LENGTH_LONG).show()
//    }

    var binding: ActivitySwipeRefreshBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_swipe_refresh)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_swipe_refresh)
//        listAdapter = adapter
        val recycleView = findViewById<RecyclerView>(R.id.recycleView)
        recycleView.adapter = adapter
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.addItemDecoration(DividerItemDecoration(
            recycleView.context, DividerItemDecoration.VERTICAL))


        //Refresh

        swipeRefreshLayout.setOnRefreshListener {
//            titles.clear()
            loadList()
        }
        loadList()
//        val linearLayout =findViewById<LinearLayout>(R.id.linearLayout)

    }
}

