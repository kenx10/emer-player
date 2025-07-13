# EmerPlayer - Android Audio Player

A modern Android audio player built with Kotlin and Jetpack Compose, featuring advanced playback controls and a clean Material 3 UI.

## Features

### Core Audio Features
- **Full Audio Playback**: Play, pause, stop, next, previous controls
- **Seek Support**: Drag to seek to any position in the track
- **Background Playback**: Continues playing when app is in background
- **Playlist Management**: Browse and play from your music library
- **Shuffle & Repeat**: Multiple repeat modes (off, all, one) and shuffle

### User Interface
- **Material 3 Design**: Modern, clean UI with dynamic theming
- **Jetpack Compose**: Fully built with Compose for smooth animations
- **Mini Player**: Persistent mini player for quick access
- **Album Art**: Displays album artwork when available
- **Search**: Search through your music library
- **Now Playing Screen**: Full-screen player with large album art

### Advanced Features
- **Volume Control**: Built-in volume slider
- **Progress Display**: Shows current time and track duration
- **Notification Controls**: Media controls in notification
- **Permission Management**: Handles audio permissions gracefully
- **Modern Architecture**: MVVM pattern with ViewModels and Repository

## Technical Stack

### Core Technologies
- **Kotlin**: 100% Kotlin codebase
- **Jetpack Compose**: Modern UI toolkit
- **Material 3**: Latest Material Design components
- **ExoPlayer**: Google's media player for Android
- **Media3**: Latest media playback APIs

### Architecture
- **MVVM Pattern**: Clean separation of concerns
- **Repository Pattern**: Data layer abstraction
- **StateFlow**: Reactive state management
- **Coroutines**: Asynchronous programming

### Dependencies
- **Compose BOM**: Latest Compose components
- **ExoPlayer**: Media playback engine
- **Media3**: Media session and controls
- **Coil**: Image loading for album art
- **Material Icons**: Extended icon set

## Project Structure

```
app/
├── src/main/java/com/emerplayer/audio/
│   ├── MainActivity.kt                 # Main entry point
│   ├── model/                         # Data models
│   │   ├── AudioTrack.kt              # Audio track model
│   │   ├── PlayerState.kt             # Player state model
│   │   ├── PlaybackState.kt           # Playback state enum
│   │   └── RepeatMode.kt              # Repeat mode enum
│   ├── repository/                    # Data layer
│   │   └── AudioRepository.kt         # Audio data repository
│   ├── player/                        # Audio player logic
│   │   └── AudioPlayerManager.kt      # Player management
│   ├── service/                       # Background services
│   │   └── AudioPlayerService.kt      # Background playback service
│   ├── viewmodel/                     # ViewModels
│   │   └── AudioPlayerViewModel.kt    # Main player ViewModel
│   ├── ui/                           # UI components
│   │   ├── theme/                    # Theme configuration
│   │   ├── screen/                   # Main screens
│   │   └── components/               # Reusable components
│   └── utils/                        # Utility classes
└── src/main/res/                     # Resources
    ├── values/                       # Strings, colors, themes
    ├── drawable/                     # Drawable resources
    └── mipmap/                       # App icons
```

## Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 26 (API level 26) or higher
- Kotlin 1.9.10 or later

### Installation
1. Clone the repository
2. Open in Android Studio
3. Sync project with Gradle files
4. Run on device or emulator

### Permissions
The app requires the following permissions:
- `READ_MEDIA_AUDIO` (Android 13+)
- `READ_EXTERNAL_STORAGE` (Android 12 and below)
- `WAKE_LOCK` (for background playback)
- `FOREGROUND_SERVICE` (for background playback)

## Usage

1. **Grant Permissions**: Allow access to media files when prompted
2. **Browse Music**: Your music library will load automatically
3. **Play Music**: Tap any track to start playback
4. **Control Playback**: Use the mini player or expand to full screen
5. **Search**: Use the search bar to find specific songs
6. **Shuffle & Repeat**: Toggle shuffle and repeat modes as needed

## Architecture Overview

### Data Flow
1. **Repository** scans device storage for audio files
2. **ViewModel** manages UI state and user interactions
3. **AudioPlayerManager** handles playback logic
4. **UI Components** display data and respond to user input

### State Management
- **PlayerState**: Central state for playback status
- **StateFlow**: Reactive state updates
- **Compose State**: UI state management

### Background Playback
- **MediaSessionService**: Handles background playback
- **ExoPlayer**: Media playback engine
- **Notifications**: Media controls in notification panel

## Key Components

### AudioPlayerManager
- Manages ExoPlayer instance
- Handles playlist management
- Provides playback state updates
- Implements shuffle and repeat logic

### AudioRepository
- Queries MediaStore for audio files
- Provides search functionality
- Manages audio metadata

### UI Components
- **MiniPlayer**: Compact player controls
- **PlayerControls**: Full player controls
- **TrackItem**: Individual track display
- **SearchBar**: Music search interface

## Advanced Features

### Shuffle Algorithm
- True random shuffle (no repeats until all played)
- Maintains shuffle state across sessions

### Repeat Modes
- **Off**: Play once and stop
- **All**: Repeat entire playlist
- **One**: Repeat current track

### Volume Control
- System volume integration
- Smooth volume transitions
- Visual volume indicator

## Performance Optimizations

- **Lazy Loading**: Tracks loaded on demand
- **Image Caching**: Album art cached efficiently
- **Background Processing**: Heavy operations on background threads
- **Memory Management**: Proper cleanup of resources

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Google's ExoPlayer team for the excellent media player
- Android Jetpack Compose team for the UI toolkit
- Material Design team for the design system
