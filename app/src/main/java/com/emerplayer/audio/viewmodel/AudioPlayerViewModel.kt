package com.emerplayer.audio.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.emerplayer.audio.model.AudioTrack
import com.emerplayer.audio.model.PlayerState
import com.emerplayer.audio.player.AudioPlayerManager
import com.emerplayer.audio.repository.AudioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AudioPlayerViewModel(application: Application) : AndroidViewModel(application) {
    
    private val audioRepository = AudioRepository(application)
    private val playerManager = AudioPlayerManager(application)
    
    private val _tracks = MutableStateFlow<List<AudioTrack>>(emptyList())
    val tracks: StateFlow<List<AudioTrack>> = _tracks.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _filteredTracks = MutableStateFlow<List<AudioTrack>>(emptyList())
    val filteredTracks: StateFlow<List<AudioTrack>> = _filteredTracks.asStateFlow()
    
    val playerState: StateFlow<PlayerState> = playerManager.playerState
    
    init {
        loadTracks()
    }
    
    fun loadTracks() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val allTracks = audioRepository.getAllAudioTracks()
                _tracks.value = allTracks
                _filteredTracks.value = allTracks
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun searchTracks(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            val filtered = if (query.isEmpty()) {
                _tracks.value
            } else {
                audioRepository.searchTracks(query)
            }
            _filteredTracks.value = filtered
        }
    }
    
    fun playTrack(track: AudioTrack) {
        val trackList = _filteredTracks.value
        val index = trackList.indexOf(track)
        if (index != -1) {
            playerManager.setPlaylist(trackList, index)
            playerManager.play()
        }
    }
    
    fun playPlaylist(tracks: List<AudioTrack>, startIndex: Int = 0) {
        playerManager.setPlaylist(tracks, startIndex)
        playerManager.play()
    }
    
    fun play() {
        playerManager.play()
    }
    
    fun pause() {
        playerManager.pause()
    }
    
    fun stop() {
        playerManager.stop()
    }
    
    fun next() {
        playerManager.next()
    }
    
    fun previous() {
        playerManager.previous()
    }
    
    fun seekTo(position: Long) {
        playerManager.seekTo(position)
    }
    
    fun toggleShuffle() {
        playerManager.toggleShuffle()
    }
    
    fun toggleRepeat() {
        playerManager.toggleRepeat()
    }
    
    fun setVolume(volume: Float) {
        playerManager.setVolume(volume)
    }
    
    override fun onCleared() {
        super.onCleared()
        playerManager.release()
    }
}