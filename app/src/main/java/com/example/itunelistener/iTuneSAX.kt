package com.example.itunelistener

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.Settings
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler
import java.net.URL
import javax.xml.parsers.SAXParserFactory


class iTuneSAX(val listener: ParserListener):DefaultHandler() {
    private val factory = SAXParserFactory.newInstance()
    private val parser = factory.newSAXParser()
    private var entryFound = false
    private var titleFound = false
    private var imageFound = false
    private var element: String = ""
    private var songTitle: String = ""
    private var songCover: Bitmap? = null
    private var songUrl: String = ""
    private val data = mutableListOf<SongData>()

    override fun startElement(
        uri: String?,
        localName: String?,
        qName: String?,
        attributes: Attributes?
    ) {
        super.startElement(uri, localName, qName, attributes)
        if (localName == "entry"){
            entryFound = true
        }
        if (entryFound){
            if (localName == "title"){
                titleFound = true
            }
            else if (localName == "image" && attributes?.getValue("height") == "170"){
                imageFound = true
            }
            else if (localName == "link" && attributes?.getValue("type")=="audio/x-m4a"){
                songUrl = attributes?.getValue("href")
            }
        }
        element = ""
    }

    override fun endElement(uri: String?, localName: String?, qName: String?) {
        super.endElement(uri, localName, qName)
        if(entryFound){
            if(titleFound){
                titleFound = false
                songTitle = element
                Log.i("Title: ", songTitle)
            }else if (imageFound){
                val url = element
                Log.i("URL", url)
                val inputStream = URL(url).openStream()
                songCover = BitmapFactory.decodeStream(inputStream)
                Log.i("isinya apaan yak: ", songCover.toString())

                imageFound = false
            }
        }
        if (localName == "entry"){
            entryFound = false
            data.add(SongData(songTitle, songCover, songUrl))
        }

    }

    override fun characters(ch: CharArray?, start: Int, length: Int) {
        super.characters(ch, start, length)
        ch?.let {
            element += String(it, start, length)
        }
    }

    fun parseURL(url: String){
        listener.start()

        GlobalScope.launch {
            try {
                val inputStream = URL(url).openStream()
                parser.parse(inputStream, this@iTuneSAX)
                withContext(Dispatchers.Main){
                    listener.finish(data)
                }
            } catch (e:Throwable) {
                e.printStackTrace()
            }
        }
    }
}