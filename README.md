# YouTube Clone Android App

A feature-rich YouTube clone Android application built with Kotlin, following modern Android development practices and clean architecture principles.

## Features

### Core Features
- 📱 Video browsing with categories
- 🎥 YouTube Shorts support
- 🔍 Video search functionality
- 📊 Channel statistics
- 🎨 Theme support (Light/Dark/System)
- 💾 Local data persistence
- 🔄 Pull-to-refresh
- 👆 Swipe-to-delete functionality

### UI/UX Features
- Material Design components
- Bottom navigation
- Category tabs with ViewPager2
- RecyclerView for video lists
- Swipe gestures
- Responsive layout
- Theme switching

## Technical Stack

### Architecture
- Clean Architecture
- MVVM (Model-View-ViewModel) pattern
- Repository pattern
- Dependency Injection with Hilt

### Libraries & Technologies
- **Language**: Kotlin
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 35 (Android 15)
- **UI Components**:
  - Material Design
  - Navigation Component
  - ViewPager2
  - RecyclerView
  - Shimmer for loading effects
  - SwipeRevealLayout

### Dependencies
- **Networking**:
  - Retrofit for API calls
  - OkHttp for HTTP client
  - YouTube Player API integration

- **Data Management**:
  - Room for local database
  - Coroutines for async operations
  - LiveData and ViewModel
  - Flow for reactive programming

- **Image Loading**:
  - Glide for image loading and caching

- **Dependency Injection**:
  - Hilt for dependency injection

## Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/tkjen/youtube/
│   │   │   ├── data/
│   │   │   │   ├── api/           # API interfaces and models
│   │   │   │   ├── local/         # Room database
│   │   │   │   ├── mapper/        # Data mappers
│   │   │   │   ├── model/         # Data models
│   │   │   │   └── repository/    # Repository implementations
│   │   │   ├── di/                # Dependency injection
│   │   │   ├── domain/            # Business logic
│   │   │   ├── ui/                # UI components
│   │   │   │   ├── home/          # Home screen
│   │   │   │   ├── shorts/        # Shorts screen
│   │   │   │   ├── library/       # Library screen
│   │   │   │   ├── settings/      # Settings screen
│   │   │   │   └── video_details/ # Video details screen
│   │   │   └── utils/             # Utility classes
│   │   └── res/                   # Resources
```

## Key Components

### 1. Application
- `YoutubeCloneApplication`: Main application class
- Theme management
- Application-wide configurations

### 2. Data Layer
- YouTube API integration
- Local database with Room
- Repository pattern implementation
- Data models and mappers

### 3. UI Layer
- Activities and Fragments
- ViewModels
- Adapters for RecyclerViews
- Custom views and components

### 4. Utils
- Theme management
- Format utilities (views, time, duration)
- Swipe-to-delete implementation
- Result wrapper for API responses

## API Integration

The app integrates with YouTube Data API v3, providing:
- Video details
- Popular videos
- Category-based search
- Channel statistics
- Shorts videos

## Setup

1. Clone the repository
2. Add your YouTube API key in `local.properties`:
   ```
   YOUTUBE_API_KEY=your_api_key_here
   ```
3. Build and run the application

## Requirements

- Android Studio Arctic Fox or newer
- JDK 11
- Android SDK 24+
- YouTube Data API v3 key

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- YouTube Data API v3
- Android Jetpack libraries
- Material Design components 