package com.emerplayer.audio.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.emerplayer.audio.R
import com.emerplayer.audio.ui.components.MiniPlayer
import com.emerplayer.audio.ui.components.PlaylistScreen
import com.emerplayer.audio.ui.components.SearchBar
import com.emerplayer.audio.viewmodel.AudioPlayerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioPlayerScreen(
    viewModel: AudioPlayerViewModel
) {
    val playerState by viewModel.playerState.collectAsState()
    val filteredTracks by viewModel.filteredTracks.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { androidx.compose.material3.Text(stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            if (playerState.currentTrack != null) {
                Surface(
                    shadowElevation = 8.dp
                ) {
                    MiniPlayer(
                        playerState = playerState,
                        onPlayPause = {
                            if (playerState.playbackState == com.emerplayer.audio.model.PlaybackState.PLAYING) {
                                viewModel.pause()
                            } else {
                                viewModel.play()
                            }
                        },
                        onNext = { viewModel.next() },
                        onPrevious = { viewModel.previous() }
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = viewModel::searchTracks,
                modifier = Modifier.padding(16.dp)
            )
            
            PlaylistScreen(
                tracks = filteredTracks,
                currentTrack = playerState.currentTrack,
                isLoading = isLoading,
                onTrackClick = { track ->
                    viewModel.playTrack(track)
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}