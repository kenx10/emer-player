package com.emerplayer.audio.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.emerplayer.audio.model.AudioTrack
import com.emerplayer.audio.model.PlaybackState
import com.emerplayer.audio.model.PlayerState
import com.emerplayer.audio.model.RepeatMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AudioPlayerManager(context: Context) {
    
    private val exoPlayer = ExoPlayer.Builder(context).build()
    
    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()
    
    private var playlist: List<AudioTrack> = emptyList()
    private var currentIndex: Int = -1
    private var isShuffleEnabled: Boolean = false
    private var repeatMode: RepeatMode = RepeatMode.OFF
    
    init {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                updatePlaybackState(state)
            }
            
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                val currentState = if (isPlaying) PlaybackState.PLAYING else PlaybackState.PAUSED
                updatePlayerState(playbackState = currentState)
            }
            
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                updateCurrentTrack()
            }
        })
    }
    
    private fun updatePlaybackState(state: Int) {
        val playbackState = when (state) {
            Player.STATE_IDLE -> PlaybackState.IDLE
            Player.STATE_BUFFERING -> PlaybackState.LOADING
            Player.STATE_READY -> if (exoPlayer.isPlaying) PlaybackState.PLAYING else PlaybackState.PAUSED
            Player.STATE_ENDED -> PlaybackState.STOPPED
            else -> PlaybackState.IDLE
        }
        updatePlayerState(playbackState = playbackState)
    }
    
    private fun updatePlayerState(
        currentTrack: AudioTrack? = _playerState.value.currentTrack,
        playbackState: PlaybackState = _playerState.value.playbackState,
        currentPosition: Long = exoPlayer.currentPosition,
        duration: Long = exoPlayer.duration.takeIf { it != C.TIME_UNSET } ?: 0L
    ) {
        _playerState.value = _playerState.value.copy(
            currentTrack = currentTrack,
            playbackState = playbackState,
            currentPosition = currentPosition,
            duration = duration,
            isShuffleEnabled = isShuffleEnabled,
            repeatMode = repeatMode,
            playlist = playlist,
            currentIndex = currentIndex
        )
    }
    
    private fun updateCurrentTrack() {
        val currentTrack = if (currentIndex in playlist.indices) {
            playlist[currentIndex]
        } else null
        updatePlayerState(currentTrack = currentTrack)
    }
    
    fun setPlaylist(tracks: List<AudioTrack>, startIndex: Int = 0) {
        playlist = tracks
        currentIndex = startIndex
        
        val mediaItems = tracks.map { track ->
            MediaItem.Builder()
                .setUri(track.uri)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(track.title)
                        .setArtist(track.artist)
                        .setAlbumTitle(track.album)
                        .setArtworkUri(track.albumArt)
                        .build()
                )
                .build()
        }
        
        exoPlayer.setMediaItems(mediaItems, startIndex, 0)
        exoPlayer.prepare()
        updateCurrentTrack()
    }
    
    fun play() {
        exoPlayer.play()
    }
    
    fun pause() {
        exoPlayer.pause()
    }
    
    fun stop() {
        exoPlayer.stop()
        updatePlayerState(playbackState = PlaybackState.STOPPED)
    }
    
    fun seekTo(position: Long) {
        exoPlayer.seekTo(position)
    }
    
    fun next() {
        if (playlist.isNotEmpty()) {
            currentIndex = getNextIndex()
            exoPlayer.seekToNext()
            updateCurrentTrack()
        }
    }
    
    fun previous() {
        if (playlist.isNotEmpty()) {
            currentIndex = getPreviousIndex()
            exoPlayer.seekToPrevious()
            updateCurrentTrack()
        }
    }
    
    private fun getNextIndex(): Int {
        return when {
            playlist.isEmpty() -> -1
            isShuffleEnabled -> {
                if (playlist.size == 1) currentIndex
                else generateSequence { (0 until playlist.size).random() }
                    .first { it != currentIndex }
            }
            repeatMode == RepeatMode.ONE -> currentIndex
            currentIndex < playlist.size - 1 -> currentIndex + 1
            repeatMode == RepeatMode.ALL -> 0
            else -> currentIndex
        }
    }
    
    private fun getPreviousIndex(): Int {
        return when {
            playlist.isEmpty() -> -1
            isShuffleEnabled -> {
                if (playlist.size == 1) currentIndex
                else generateSequence { (0 until playlist.size).random() }
                    .first { it != currentIndex }
            }
            repeatMode == RepeatMode.ONE -> currentIndex
            currentIndex > 0 -> currentIndex - 1
            repeatMode == RepeatMode.ALL -> playlist.size - 1
            else -> currentIndex
        }
    }
    
    fun toggleShuffle() {
        isShuffleEnabled = !isShuffleEnabled
        exoPlayer.shuffleModeEnabled = isShuffleEnabled
        updatePlayerState()
    }
    
    fun toggleRepeat() {
        repeatMode = when (repeatMode) {
            RepeatMode.OFF -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.OFF
        }
        
        exoPlayer.repeatMode = when (repeatMode) {
            RepeatMode.OFF -> Player.REPEAT_MODE_OFF
            RepeatMode.ALL -> Player.REPEAT_MODE_ALL
            RepeatMode.ONE -> Player.REPEAT_MODE_ONE
        }
        
        updatePlayerState()
    }
    
    fun setVolume(volume: Float) {
        exoPlayer.volume = volume
        updatePlayerState()
    }
    
    fun release() {
        exoPlayer.release()
    }
    
    fun getCurrentPosition(): Long = exoPlayer.currentPosition
    fun getDuration(): Long = exoPlayer.duration.takeIf { it != C.TIME_UNSET } ?: 0L
    fun isPlaying(): Boolean = exoPlayer.isPlaying
}