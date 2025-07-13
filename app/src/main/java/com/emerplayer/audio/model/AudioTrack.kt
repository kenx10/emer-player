package com.emerplayer.audio.model

import android.net.Uri

data class AudioTrack(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val uri: Uri,
    val albumArt: Uri? = null,
    val track: Int? = null,
    val year: Int? = null,
    val size: Long = 0L
) {
    fun getDurationString(): String {
        val minutes = duration / 60000
        val seconds = (duration % 60000) / 1000
        return String.format("%d:%02d", minutes, seconds)
    }
}