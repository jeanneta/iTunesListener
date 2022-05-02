package com.example.itunelistener

interface ParserListener {
    fun start()
    fun finish(songs: List<SongData>)
}