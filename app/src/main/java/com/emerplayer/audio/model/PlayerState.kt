package com.emerplayer.audio.model

data class PlayerState(
    val currentTrack: AudioTrack? = null,
    val playbackState: PlaybackState = PlaybackState.IDLE,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val isShuffleEnabled: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val volume: Float = 1.0f,
    val playlist: List<AudioTrack> = emptyList(),
    val currentIndex: Int = -1
)