package com.example.itunelistener

import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.itunelistener.databinding.ActivityPreviewBinding
import java.lang.Exception

class PreviewActivity : AppCompatActivity(), MediaController.MediaPlayerControl {
    private var title: String? = null
    private var cover: Bitmap? = null
    private var url: String? = null
    private lateinit var binding: ActivityPreviewBinding

    private var isPlaying = false
    private var bufferPercentage = 0

    private var mediaPlayer = MediaPlayer()
    private val mediaController: MediaController by lazy {
        object : MediaController(this) {
            override fun show(timeout: Int) {
                super.show(0)
            }

            override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
                if (event!!.keyCode == KeyEvent.KEYCODE_BACK){
                    finish()
                }
                return super.dispatchKeyEvent(event)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isPlaying", isPlaying)
        outState.putInt("currentPosition", mediaPlayer.currentPosition)
    }

    override fun onPause() {
        super.onPause()
        if (isPlaying) mediaPlayer.pause()
        mediaController.hide()
    }

    override fun onStart() {
        super.onStart()
        if (isPlaying) mediaPlayer.start()
        mediaController.show()

    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_preview)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_preview)

        title = intent.getStringExtra("title")
        cover = intent.getParcelableExtra("cover")
        url = intent.getStringExtra("url")

        binding.title = title
        binding.cover = cover


        try {
            mediaPlayer.setDataSource(url)
            mediaPlayer.setOnPreparedListener {
                Log.i("PreviewActivity", "MediaPlayer is read....")


                mediaController.setAnchorView(binding.anchorView)
                mediaController.setMediaPlayer(this)
                mediaController.show()

                if (savedInstanceState != null){
                    isPlaying = savedInstanceState.getBoolean("isPlaying")
                    val position = savedInstanceState.getInt("currentPosition")
                    mediaPlayer.seekTo(position)
                    if (isPlaying) mediaPlayer.start()
                }
            }
            mediaPlayer.setOnCompletionListener {
                isPlaying = false
            }
            mediaPlayer.prepareAsync()

        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun onPreviewClick(view: View) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun start() {
        mediaPlayer.start()
        isPlaying = true
    }

    override fun pause() {
        mediaPlayer.pause()
        isPlaying = false

    }

    override fun getDuration(): Int {
        return mediaPlayer.duration
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun seekTo(p0: Int) {
        mediaPlayer.seekTo(p0)
    }

    override fun isPlaying(): Boolean {
        return isPlaying
    }

    override fun getBufferPercentage(): Int {
        return bufferPercentage
    }

    override fun canPause(): Boolean {
        return true
    }

    override fun canSeekBackward(): Boolean {
        return true
    }

    override fun canSeekForward(): Boolean {
        return true
    }

    override fun getAudioSessionId(): Int {
        return mediaPlayer.audioSessionId
    }
}