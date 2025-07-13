package com.emerplayer.audio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.emerplayer.audio.ui.screen.AudioPlayerScreen
import com.emerplayer.audio.ui.screen.PermissionScreen
import com.emerplayer.audio.ui.theme.EmerPlayerTheme
import com.emerplayer.audio.utils.PermissionUtils
import com.emerplayer.audio.viewmodel.AudioPlayerViewModel

class MainActivity : ComponentActivity() {
    
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            // Permissions granted, refresh the UI
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EmerPlayerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainContent()
                }
            }
        }
    }
    
    @Composable
    private fun MainContent() {
        val viewModel: AudioPlayerViewModel = viewModel()
        val hasPermission = PermissionUtils.hasAudioPermission(this)
        
        if (hasPermission) {
            LaunchedEffect(Unit) {
                viewModel.loadTracks()
            }
            AudioPlayerScreen(viewModel = viewModel)
        } else {
            PermissionScreen(
                onRequestPermission = {
                    requestPermissionLauncher.launch(PermissionUtils.getRequiredPermissions())
                }
            )
        }
    }
}