package com.example.itunelistener

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.itunelistener.databinding.ActivityPreviewBinding

class PreviewActivity : AppCompatActivity() {
    private var title: String? = null
    private var cover: Bitmap? = null
    private var url: String? = null
    private var binding: ActivityPreviewBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_preview)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_preview)

        title = intent.getStringExtra("title")
        cover = intent.getParcelableExtra("cover")
        url = intent.getStringExtra("url")

        binding?.title = title
        binding?.cover = cover

//        val textView = findViewById<TextView>(R.id.textView)
//        textView.text = title
//        val imageView = findViewById<ImageView>(R.id.imageView)
//        imageView.setImageBitmap(cover)


    }

    fun onPreviewClick(view: View) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}