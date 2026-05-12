# CineStream: Movie Streaming App 🎬

A progressive Android application developed as part of **Software for Mobile Devices (SMD)** coursework at **NUCES-CFD Campus**. This project demonstrates the evolution of Android development from basic XML-driven UI to cloud-native architecture with Firebase, modern Jetpack Compose, and real-time synchronization.

**Student:** Abdulwahab (23F-3038)

---

## 📋 Project Overview

CineStream is a feature-rich movie streaming application that showcases Android development best practices across four cumulative assignments. Each assignment builds upon the previous one, introducing new technologies and design patterns.

---

## 🎯 Assignment Breakdown

### Assignment #01: Project Setup & Basic UI (Foundation)
**Concepts:** Activities, Intents, Layouts, Material Design, Intent Passing

- Created **SplashActivity** with animated introduction
- Implemented **MainActivity** with user onboarding (username, plan selection, user ID)
- Designed responsive XML layouts with Material Design principles
- Established project structure and resource organization
- **Output:** Working splash screen → main activity flow with data passing via Intent

---

### Assignment #02: Fragments, Navigation & RecyclerView (UI Enhancement)
**Concepts:** Fragments, Fragment Transactions, BottomNavigationView, RecyclerView, Adapters

#### Features Implemented:
- ✅ **Fragment-based Navigation** — 5 main fragments via BottomNavigationView
  - HomeFragment (movie listing)
  - SearchFragment (with SearchView filtering)
  - DiscoverFragment (placeholder for API content)
  - WatchlistFragment (bookmarked movies)
  - ProfileFragment (user account & stats)

- ✅ **RecyclerView with MovieRecyclerAdapter**
  - Custom ViewHolder binding
  - Movie card layout with poster, title, genre, rating
  - Click listeners for detail navigation

- ✅ **MovieDetailFragment**
  - Full movie information display
  - "Add to Watchlist" button
  - Back navigation with Fragment transaction

- ✅ **SearchView Integration**
  - Real-time movie filtering via `MovieDataProvider.searchMovies()`
  - Case-insensitive search

#### Project Structure (A2):
```
app/src/main/java/com/example/moviestreamingapp/
├── activities/
│   ├── SplashActivity.kt
│   └── MainActivity.kt
├── fragments/
│   ├── HomeFragment.kt
│   ├── MovieDetailFragment.kt
│   ├── SearchFragment.kt
│   ├── DiscoverFragment.kt
│   ├── WatchlistFragment.kt
│   └── ProfileFragment.kt
├── adapters/
│   └── MovieRecyclerAdapter.kt
├── models/
│   └── Movie.kt (Serializable)
└── utils/
    └── MovieDataProvider.kt (12 hardcoded movies)

res/layout/
├── activity_main.xml
├── fragment_home.xml
├── fragment_search.xml
├── fragment_movie_detail.xml
├── fragment_watchlist.xml
├── fragment_profile.xml
└── item_movie_recycler.xml

res/menu/
└── bottom_nav_menu.xml
```

---

### Assignment #03: REST API & SQLite Database (Data Integration)
**Concepts:** REST APIs, Retrofit, Coroutines, SQLite, SQLiteOpenHelper, CRUD Operations

#### Features Implemented:
- ✅ **REST API Integration (TVMaze API)**
  - Retrofit + Gson for serialization
  - `ApiService.kt` with `getShows()` endpoint
  - Asynchronous API calls using Kotlin Coroutines
  - Error handling & loading states

- ✅ **SQLite Database with 2 Tables**
  - **watchlist** table (id, movie_id, title, genre, rating, added_date)
  - **watch_progress** table (id, watchlist_id, progress, last_watched, FK constraint)
  - `DatabaseHelper.kt` extending SQLiteOpenHelper

- ✅ **Full CRUD Operations**
  - `addToWatchlist()` — Insert movie to watchlist
  - `getAllWatchlist()` — Retrieve all bookmarked movies
  - `updateProgress()` — Track watch history
  - `removeFromWatchlist()` — Delete from watchlist
  - `searchWatchlist(query)` — LIKE-based search
  - `getWatchlistSortedByRating()` — ORDER BY rating
  - `isInWatchlist(id)` — Check existence

- ✅ **Fragments for API & Local Data**
  - **ApiMoviesFragment** — Display TVMaze shows with RecyclerView
  - **WatchlistFragment** — Local watchlist with search, sort, delete
  - Integration with MovieDetailFragment for adding to watchlist

#### Key Classes (A3):
```
java/com/example/moviestreamingapp/
├── network/
│   ├── ApiService.kt
│   └── RetrofitClient.kt
├── database/
│   └── DatabaseHelper.kt
├── models/
│   ├── Movie.kt (from A2)
│   ├── ApiMovie.kt
│   ├── ApiRating.kt
│   └── WatchlistItem.kt
└── adapters/
    ├── MovieRecyclerAdapter.kt
    ├── ApiMovieAdapter.kt
    └── WatchlistAdapter.kt

res/layout/
├── fragment_api_movies.xml
├── fragment_watchlist.xml
├── item_api_movie.xml
└── item_watchlist.xml
```

#### Dependencies Added (A3):
```gradle
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
```

---

### Assignment #04: Firebase, Push Notifications & Jetpack Compose (Cloud Native)
**Concepts:** Firebase Authentication, Firestore, FCM, Jetpack Compose, Push Notifications

#### Functional Requirements:

##### F1: Firebase Authentication ✅
- **Multiple Sign-In Methods:**
  - Email/Password authentication
  - Google Sign-In (OAuth)
- **Session Management:**
  - Persistent user sessions across app restarts
  - Logout functionality
- **UI Flows:**
  - Login screen
  - Registration screen
  - Auth state persistence

**Implementation Classes:**
- `AuthRepository.kt` — Firebase Auth logic
- `LoginFragment.kt` / `SignUpFragment.kt` — Auth UI (Fragments)
- `signInWithGoogle()` — Google OAuth token exchange
- `signInWithEmail()` — Email/password authentication

---

##### F2: Firestore Real-Time Database ✅
- **Data Collections:**
  - `users` — User profiles & metadata
  - `watchlists` — Synchronized watchlist across devices
  - Real-time listeners for instant sync

- **Real-Time Synchronization:**
  - LiveData/StateFlow for UI updates
  - Automatic merge of changes from multiple devices
  - Offline persistence support

**Implementation Classes:**
- `FirestoreHelper.kt` — Firestore operations
  - `syncUserData()` — Listen for real-time user updates
  - `syncWatchlist()` — Real-time watchlist synchronization
  - `uploadWatchlistItem()` — Add movie to cloud
  - `deleteWatchlistItem()` — Remove from cloud

---

##### F3: Push Notifications (FCM) ✅
- **Firebase Cloud Messaging Integration**
  - Server sends notifications about new movies, watchlist updates
  - Custom notification handlers
  - Topic-based subscriptions

**Implementation Classes:**
- `MyFirebaseMessagingService.kt` — Extends FirebaseMessagingService
  - `onMessageReceived()` — Handle incoming notifications
  - `sendNotification()` — Create and display notifications
- Manifest permissions: `com.google.android.c2dm.permission.RECEIVE`

---

##### F4: Jetpack Compose Implementation ✅
- **Modernized UI Screens:**
  - Watchlist screen redesigned in Jetpack Compose
  - Movie card component using Compose
  - Search filter UI with Compose
  - Smooth animations & transitions

**Implementation Files:**
- `ComposeWatchlistScreen.kt` — Main watchlist UI
- `MovieCardCompose.kt` — Reusable movie card component
- `build.gradle.kts` includes Compose dependencies

---

##### F5 & F6: Self-Researched Features 🔍
(2+ innovative features beyond curriculum)

**Feature 1: [To Be Specified]**
- Implementation & Research Documentation
- GitHub references included in commits

**Feature 2: [To Be Specified]**
- Advanced Android capability
- Documented with tutorial/API references

---

## 🏗️ Complete Project Architecture

```
MovieStreamingApp/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/moviestreamingapp/
│   │   │   ├── activities/
│   │   │   │   ├── SplashActivity.kt
│   │   │   │   ├── MainActivity.kt
│   │   │   │   └── AuthActivity.kt (A4)
│   │   │   │
│   │   │   ├── fragments/
│   │   │   │   ├── HomeFragment.kt
│   │   │   │   ├── SearchFragment.kt
│   │   │   │   ├── DiscoverFragment.kt (ApiMoviesFragment)
│   │   │   │   ├── WatchlistFragment.kt
│   │   │   │   ├── ProfileFragment.kt
│   │   │   │   ├── MovieDetailFragment.kt
│   │   │   │   ├── LoginFragment.kt (A4)
│   │   │   │   ├── SignUpFragment.kt (A4)
│   │   │   │   └── ComposeWatchlistFragment.kt (A4)
│   │   │   │
│   │   │   ├── adapters/
│   │   │   │   ├── MovieRecyclerAdapter.kt
│   │   │   │   ├── ApiMovieAdapter.kt
│   │   │   │   └── WatchlistAdapter.kt
│   │   │   │
│   │   │   ├── models/
│   │   │   │   ├── Movie.kt
│   │   │   │   ├── ApiMovie.kt
│   │   │   │   ├── ApiRating.kt
│   │   │   │   ├── WatchlistItem.kt
│   │   │   │   ├── User.kt (A4)
│   │   │   │   └── Notification.kt (A4)
│   │   │   │
│   │   │   ├── network/
│   │   │   │   ├── ApiService.kt
│   │   │   │   └── RetrofitClient.kt
│   │   │   │
│   │   │   ├── database/
│   │   │   │   └── DatabaseHelper.kt
│   │   │   │
│   │   │   ├── firebase/ (A4)
│   │   │   │   ├── AuthRepository.kt
│   │   │   │   ├── FirestoreHelper.kt
│   │   │   │   ├── MyFirebaseMessagingService.kt
│   │   │   │   └── NotificationManager.kt
│   │   │   │
│   │   │   ├── compose/ (A4)
│   │   │   │   ├── screens/
│   │   │   │   │   └── ComposeWatchlistScreen.kt
│   │   │   │   ├── components/
│   │   │   │   │   ├── MovieCardCompose.kt
│   │   │   │   │   └── SearchFilterCompose.kt
│   │   │   │   └── theme/
│   │   │   │       └── ComposeTheme.kt
│   │   │   │
│   │   │   ├── utils/
│   │   │   │   └── MovieDataProvider.kt
│   │   │   │
│   │   │   └── viewmodels/ (A4)
│   │   │       ├── AuthViewModel.kt
│   │   │       ├── WatchlistViewModel.kt
│   │   │       └── NotificationViewModel.kt
│   │   │
│   │   ├── AndroidManifest.xml
│   │   │
│   │   └── res/
│   │       ├── layout/
│   │       │   ├── activity_splash.xml
│   │       │   ├── activity_main.xml
│   │       │   ├── activity_auth.xml (A4)
│   │       │   ├── fragment_home.xml
│   │       │   ├── fragment_search.xml
│   │       │   ├── fragment_movie_detail.xml
│   │       │   ├── fragment_watchlist.xml
│   │       │   ├── fragment_profile.xml
│   │       │   ├── fragment_login.xml (A4)
│   │       │   ├── fragment_signup.xml (A4)
│   │       │   ├── item_movie_recycler.xml
│   │       │   ├── item_api_movie.xml
│   │       │   ├── item_watchlist.xml
│   │       │   └── notification_layout.xml (A4)
│   │       │
│   │       ├── menu/
│   │       │   └── bottom_nav_menu.xml
│   │       │
│   │       ├── values/
│   │       │   ├── strings.xml
│   │       │   ├── colors.xml
│   │       │   ├── dimens.xml
│   │       │   └── themes.xml
│   │       │
│   │       ├── drawable/
│   │       │   └── [app icons & graphics]
│   │       │
│   │       └── mipmap/
│   │           └── [launcher icons]
│   │
│   └── build.gradle.kts
│
├── build.gradle.kts
├── settings.gradle.kts
├── google-services.json (A4 — Firebase config)
└── .gitignore

Documentation/
├── logic_map.pdf (A4 requirements mapping)
├── ARCHITECTURE.md (System design overview)
└── FIREBASE_SETUP.md (Configuration guide)
```

---

## 💻 Tech Stack

### Core Android Framework
- **Min SDK:** API 24 (Android 7.0)
- **Target SDK:** API 36 (Android 15)
- **Language:** Kotlin
- **Build System:** Gradle KTS

### Architecture & Patterns
- **Fragments** — Modular UI composition
- **MVVM** — Model-View-ViewModel (with ViewModels)
- **Repository Pattern** — Data abstraction layer
- **LiveData/StateFlow** — Reactive data binding

### UI Framework
- **Material Design 3** — XML layouts (A1-A3)
- **Jetpack Compose** — Modern declarative UI (A4)
- **ConstraintLayout** — Responsive layouts
- **RecyclerView** — Efficient list rendering

### Data & Networking
- **Retrofit 2.9.0** — REST API client
- **Gson** — JSON serialization
- **Kotlin Coroutines** — Async/background tasks
- **SQLite + SQLiteOpenHelper** — Local persistence
- **OkHttp3** — HTTP logging & interceptors

### Cloud & Services
- **Firebase Authentication** — Email/Password & Google Sign-In (A4)
- **Cloud Firestore** — Real-time NoSQL database (A4)
- **Firebase Cloud Messaging** — Push notifications (A4)
- **Google Play Services** — OAuth & API support

### External APIs
- **TVMaze API** — Movie/show data (free, no key required)
- **Google OAuth 2.0** — Social authentication

### Testing & Debugging
- **Logcat** — Log monitoring
- **Android Emulator** — API 36.1 (Medium Phone)
- **Google Play Console** — Firebase project management

---

## 📦 Dependencies Summary

```gradle
// Core Android
implementation("androidx.core:core-ktx:1.12.0")
implementation("androidx.appcompat:appcompat:1.6.1")
implementation("androidx.fragment:fragment-ktx:1.6.2")
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

// Layouts & Navigation
implementation("androidx.constraintlayout:constraintlayout:2.1.4")
implementation("androidx.coordinatorlayout:coordinatorlayout:1.3.0")
implementation("androidx.gridlayout:gridlayout:1.1.0")
implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
implementation("androidx.navigation:navigation-ui-ktx:2.7.6")

// Material Design
implementation("com.google.android.material:material:1.11.0")

// Networking
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

// Async
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

// Firebase (A4)
implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
implementation("com.google.firebase:firebase-auth-ktx")
implementation("com.google.firebase:firebase-firestore-ktx")
implementation("com.google.firebase:firebase-messaging-ktx")

// Jetpack Compose (A4)
implementation("androidx.compose.ui:ui:1.6.4")
implementation("androidx.compose.material3:material3:1.2.1")
implementation("androidx.compose.foundation:foundation:1.6.4")
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

// RecyclerView
implementation("androidx.recyclerview:recyclerview:1.3.2")

// Testing (optional)
testImplementation("junit:junit:4.13.2")
androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
```

---

## 🚀 Setup & Installation

### Prerequisites
- Android Studio (latest)
- JDK 11 or higher
- Android SDK (API 24-36)
- Git
- Firebase account (for A4)

### Step 1: Clone Repository
```bash
git clone https://github.com/YOUR_USERNAME/MovieStreamingApp.git
cd MovieStreamingApp
```

### Step 2: Open in Android Studio
1. Open Android Studio
2. File → Open → Select project directory
3. Wait for Gradle sync to complete

### Step 3: Firebase Setup (Assignment #04)
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create new project: "CineStream-SMD"
3. Enable Authentication:
   - Email/Password
   - Google Sign-In
4. Create Firestore Database (Start in test mode)
5. Collections to create:
   - `users` — User profiles
   - `watchlists` — Movie bookmarks
6. Download `google-services.json`
7. Place in `app/` directory
8. Add Firebase plugin to `build.gradle.kts`:
   ```gradle
   plugins {
       id("com.google.gms.google-services") version "4.4.0"
   }
   ```

### Step 4: Google Sign-In Configuration
1. Get SHA-1 fingerprint:
   ```bash
   ./gradlew signingReport
   ```
2. Add to Firebase Console → Authentication → Google → Web SDK configuration
3. Generate OAuth 2.0 credentials (Web application) in Google Cloud Console
4. Add `google_client_id` to `strings.xml`:
   ```xml
   <string name="google_web_client_id">YOUR_WEB_CLIENT_ID.apps.googleusercontent.com</string>
   ```

### Step 5: Run the App
1. Start Android Emulator (API 36.1)
2. Press **Run ▶** in Android Studio
3. Wait for APK build & installation (~1-2 minutes)

---

## 📱 App Features by Fragment

| Fragment | Features | Data Source | Assignment |
|----------|----------|-------------|-----------|
| **Home** | Movie listing, RecyclerView, trending movies | Local (hardcoded) | A2 |
| **Search** | Real-time filtering, SearchView | Local movies | A2 |
| **Discover** | TVMaze API integration, loading states | REST API | A3 |
| **Watchlist** | Add/remove movies, search, sort by rating, CRUD | SQLite DB | A3 |
| **Movie Detail** | Full info, add to watchlist, back navigation | Local/SQLite | A2-A3 |
| **Login** | Email/Password, Google Sign-In | Firebase Auth | A4 |
| **Sign Up** | Registration form, email verification | Firebase Auth | A4 |
| **Profile** | User account, subscription plan, stats | Firebase + Local | A4 |
| **Compose Watchlist** | Modern UI, animations, real-time sync | Firestore | A4 |

---

## 🔐 Firebase Security Rules

### Firestore Rules (Test Mode → Production)
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Only authenticated users can read/write their own data
    match /users/{userId} {
      allow read, write: if request.auth.uid == userId;
    }
    
    // Watchlist: user-specific
    match /watchlists/{userId}/{document=**} {
      allow read, write: if request.auth.uid == userId;
    }
  }
}
```

---

## 🔄 GitHub Collaboration Strategy

### Branch Structure
```
main (production)
├── feature/firebase-auth
├── feature/firestore-sync
├── feature/fcm-notifications
├── feature/compose-ui
├── feature/self-research-1
└── feature/self-research-2
```

### Commit Conventions
```
[A4] feat: Implement Firebase Authentication with Google Sign-In
[A4] feat: Add Firestore real-time synchronization for watchlist
[A4] feat: Integrate FCM push notifications
[A4] feat: Redesign watchlist with Jetpack Compose
[A4] feat: Implement [Feature Name] - Research documentation included
[A4] docs: Update logic_map.pdf with component mappings
[A3] fix: Resolve SQLite constraint error in watchlist CRUD
```

### Pull Request Template
```
## Feature Description
Implements [Requirement ID]: [Feature Name]

## Related Assignment
- Assignment #04
- Requirements: F1, F2

## Implementation Details
- Firebase Auth with Email + Google
- Real-time Firestore sync using LiveData
- Custom FCM service for notifications

## Testing Done
- ✅ Tested on Android API 36.1 emulator
- ✅ Verified Firebase configuration
- ✅ Tested multi-device synchronization

## References
- https://firebase.google.com/docs/auth
- https://firebase.google.com/docs/firestore

## Reviewers
@teammate1 @teammate2
```

---

## 📖 Documentation & References

### Official Documentation
- [Android Developers](https://developer.android.com/)
- [Firebase Docs](https://firebase.google.com/docs)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Retrofit Documentation](https://square.github.io/retrofit/)

### Project Documents
- **logic_map.pdf** — Requirements → Code mapping
- **FIREBASE_SETUP.md** — Firebase configuration guide
- **ARCHITECTURE.md** — System design & data flow diagrams

### External APIs
- [TVMaze API](https://www.tvmaze.com/api) — Show data (no authentication)
- [Google OAuth 2.0](https://developers.google.com/identity/protocols/oauth2)

---
## 🎓 Learning Outcomes

### Android Fundamentals
- ✅ Activity lifecycle & Intent-based communication
- ✅ Fragment architecture & transaction management
- ✅ RecyclerView for efficient list rendering
- ✅ Material Design 3 & responsive layouts

### Data Management
- ✅ REST API integration with Retrofit
- ✅ SQLite database design & CRUD operations
- ✅ Firebase Firestore for real-time sync
- ✅ Async programming with Kotlin Coroutines

### Modern Android Development
- ✅ Jetpack Compose for declarative UI
- ✅ ViewModel & LiveData for state management
- ✅ Firebase Authentication & Cloud Messaging
- ✅ MVVM architecture pattern

### Professional Development
- ✅ Git version control & branching strategy
- ✅ Code documentation & README best practices
- ✅ Self-directed learning & research
- ✅ Collaborative development workflows

---

## 🐛 Troubleshooting

### Build Issues
**Error:** `Gradle project sync failed`
- **Solution:** File → Invalidate Caches → Restart Android Studio

**Error:** `Unresolved reference 'R'`
- **Solution:** Build → Clean Project, then Rebuild Project

**Error:** `adb: no devices/emulators found`
- **Solution:** Open Device Manager and click Play ▶ to start emulator

### Firebase Issues
**Error:** `FirebaseApp is not initialized`
- **Solution:** Ensure `google-services.json` is in `app/` directory

**Error:** `GoogleSignInAccount is null`
- **Solution:** Verify SHA-1 fingerprint matches Firebase Console configuration

**Error:** `Firestore permission denied`
- **Solution:** Update Firestore rules to allow authenticated users

### Runtime Issues
**Crash:** `NullPointerException in MovieDetailFragment`
- **Solution:** Check that Movie object is properly passed via Bundle

**Issue:** **Notifications not appearing**
- **Solution:** Verify FCM permissions in AndroidManifest.xml & Firebase project settings

---

## 📧 Contact & Support

**Student Name:** Abdulwahab  
**Roll Number:** 23F-3038  
**Course:** Software for Mobile Devices (SMD)  
**Institution:** NUCES-CFD Campus, Faisalabad

For queries or clarifications:
- Refer to course instructors
- Check official Android documentation
- Review Firebase setup guide

---

## 📝 License

This project is submitted as coursework for academic purposes at NUCES-CFD Campus. Unauthorized copying or plagiarism is strictly prohibited per institutional policies.
--

## 🙏 Acknowledgments

- Course instructors for clear assignment specifications
- Android & Firebase teams for excellent documentation
- NUCES-CFD for providing development resources
---

### Quick Links
- 📦 [Build APK](#setup--installation)
- 🚀 [Run on Emulator](#step-5-run-the-app)
- 🔐 [Firebase Config](#step-3-firebase-setup-assignment-04)
- 📖 [Architecture Overview](#-complete-project-architecture)
- ✅ [Submission Checklist](#-submission-checklist)
