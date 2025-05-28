# OuiOui – Travel Explorer App 🌍

*OuiOui* is a Kotlin-based Android application that allows users to explore, search, and save beautiful travel destinations from around the world. Built using a modern architecture and tech stack, it’s the final project for the Mobile App Development course at Reichman University.

---

## 📱 Features

- 🗺 Discover trending travel destinations
- 🔍 Search by name or category
- 💾 Save favorite locations locally
- 📄 View detailed information (images, description, etc.)
- 🌙 Dark mode support
- ⚡ Works offline for saved places

---

## 🏗 Tech Stack

- *Kotlin* + *MVVM Architecture*
- *Retrofit* – API communication
- *Room* – Local database (offline storage)
- *Navigation Component* – Smooth navigation between fragments
- *Dagger-Hilt* – Dependency injection
- *Coroutines* – Asynchronous operations
- *Glide* – Image loading

---

## 🧠 Architecture

<pre> 
com.ouioui.app
├── data
│   ├── remote        // Retrofit API service and response models
│   ├── local         // Room database, DAOs, and entities
│   └── model         // Shared data models used across the app
│
├── repository        // Implements data access logic using remote and local sources
│
├── ui
│   ├── home          // Home screen (popular destinations)
│   ├── search        // Search screen with filters
│   ├── details       // Detailed view for a destination
│   ├── saved         // User's saved/favorite destinations
│   ├── category      // Category-wise browsing (beach, mountain, etc.)
│   └── settings      // Settings screen (dark mode toggle, etc.)
│
├── di
│   └── modules       // Hilt modules for dependency injection (Retrofit, Room, etc.)
│
├── utils             // Utility classes, constants, and extensions
│
└── MainActivity.kt   // App entry point, contains NavHostFragment </pre>

---

## 👥 Team Roles

| Name            | Role / Focus Area                 | Responsibilities |
|-----------------|-----------------------------------|------------------|
| *Raz Bouganim* | 🌐 API Integration & Home Fragment | <ul><li>Setup Retrofit & remote data layer</li><li>Define API endpoints & models</li><li>Implement Home Fragment (popular destinations)</li><li>Handle remote Repository logic (network calls)</li><li>Loading/error states for remote data</li></ul> |
| *Ohad Cohen*   | 💾 Local DB & Saved Fragment        | <ul><li>Configure Room DB (entities, DAOs)</li><li>Implement Saved Fragment (add/remove saved places)</li></ul> |
| *Noam Mellul*  | 🎨 UI/UX, Category & Settings       | <ul><li>Implement Category Fragment</li><li>Build Settings Fragment (dark mode)</li><li>Style the app: themes, fonts, spacing</li><li>Set up Navigation (NavHostFragment + graph)</li><li>Integrate Glide for image loading</li></ul> |

---

## 📄 License
This project is part of the final coursework at Reichman University.
