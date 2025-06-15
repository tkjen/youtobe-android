

---

# YouTube Clone Android App

An Android application that mimics YouTube, built with Kotlin using clean architecture and modern libraries.

## Main Features

### 1. Home
- Watch videos by category (All, Music, Gaming, Sports, News)
- Display popular/trending videos
- Search for videos
- View channel information

### 2. Shorts
- Watch short videos
- Swipe up/down to watch the next video
- Load more videos on scroll

### 3. Library
- View watch history
- Manage liked videos
- Swipe to delete videos

### 4. Settings
- Switch theme (Light/Dark/System)
- Manage account
- App settings

### 5. Video Details
- Watch the video
- View video information
- View channel statistics
- Interact with the video (like, comment, etc.)

## Tech Stack

- **Language**: Kotlin
- **Architecture**: Clean Architecture + MVVM
- **Core Libraries**:
  - Hilt: Dependency Injection
  - Retrofit: YouTube API calls
  - Room: Local storage
  - Coroutines: Asynchronous processing
  - Navigation: Screen navigation
  - ViewPager2: Switching between categories
  - Glide: Image loading
  - Material Design: UI components

## Project Structure

```
app/
├── data/           # Data Layer
│   ├── api/        # YouTube API
│   ├── local/      # Local Database
│   ├── model/      # Data Models
│   └── repository/ # Repositories
├── di/             # Dependency Injection
├── domain/         # Domain Layer (Business Logic)
├── ui/             # UI Layer (Views)
│   ├── home/       # Home Screen
│   ├── shorts/     # Shorts Screen
│   ├── library/    # Library Screen
│   ├── settings/   # Settings Screen
│   └── video_details/ # Video Details Screen
└── utils/          # Utility classes
```
