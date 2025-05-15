# Movies App

A modern Android application for browsing and managing your favorite movies. This app follows clean architecture principles and is built with Jetpack Compose for a beautiful, modern UI.

## 📱 Features

- Browse popular movies
- Search for movies
- View detailed movie information
- See movie credits and cast
- Discover similar movies
- Add movies to your watchlist
- Material 3 Design with dynamic theming

## 🏗️ Architecture

This application follows **Clean Architecture** principles and is structured in layers:

### 📱 Presentation Layer

The presentation layer is built using **Jetpack Compose** and follows the **MVVM** (Model-View-ViewModel) pattern:

- **Views**: Composable functions that render UI elements
- **ViewModels**: Manage UI state and handle user interactions
- **States**: Immutable classes for UI state representation
- **Events**: User actions that trigger state changes
- **Effects**: One-time events like navigation or showing snackbars

### 🧠 Domain Layer

The domain layer contains the business logic of the application:

- **Use Cases**: Single responsibility classes that execute specific business logic
- **Repository Interfaces**: Define contracts for data operations
- **Models**: Business models representing core entities

### 💾 Data Layer

The data layer handles data operations and is divided into:

- **Repository Implementation**: Implements the repository interfaces from the domain layer
- **Data Sources**:
  - **Remote**: Communicates with the TMDB API using Ktor
  - **Local**: Manages local database operations with Room
- **Models**: Data models for API responses and database entities

## 📊 Dependency Injection

The app uses **Koin** for dependency injection, organized into three modules:

- **App Module**: Provides app-level dependencies like ViewModels and utilities
- **Domain Module**: Provides use cases and repository interfaces
- **Data Module**: Provides repository implementations and data sources

## 🧪 Testing

The app includes comprehensive unit tests for:

- Use cases
- Repositories
- ViewModels
- Data sources

Tests are written using **JUnit**, **MockK**, **Turbine**, and **Google Truth** for assertions.

## 📚 Tech Stack

### Core Libraries

- **Kotlin**: Primary programming language
- **Coroutines & Flow**: Asynchronous programming
- **Jetpack Compose**: UI toolkit
- **Koin**: Dependency injection
- **Ktor**: HTTP client
- **Room**: Local database
- **Kotlinx Serialization**: JSON parsing
- **Material 3**: UI design system

### Architecture Components

- **ViewModel**: Manage UI-related data
- **Navigation**: Jetpack Navigation Compose
- **StateFlow & SharedFlow**: Stream UI states and events

### Image Loading

- **Coil**: Image loading and caching

### Testing Libraries

- **JUnit**: Testing framework
- **MockK**: Mocking library
- **Turbine**: Flow testing
- **Google Truth**: Assertion library
- **Coroutines Test**: Test utilities for coroutines

## 📦 Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/io/mohammedalaamorsi/movies/
│   │   │   ├── data/
│   │   │   │   ├── locale/     # Room database, DAOs and local data source
│   │   │   │   ├── models/     # Data models and entities
│   │   │   │   ├── remote/     # API client and remote data source
│   │   │   │   └── repository/ # Repository implementations
│   │   │   │
│   │   │   ├── di/
│   │   │   │   ├── AppModule.kt    # ViewModels and utilities
│   │   │   │   ├── DataModule.kt   # Data sources and repositories
│   │   │   │   └── DomanModule.kt  # Use cases and domain layer
│   │   │   │
│   │   │   ├── domain/
│   │   │   │   ├── usecase/        # Business logic use cases
│   │   │   │   └── MoviesRepository.kt # Repository interface
│   │   │   │
│   │   │   ├── navigation/         # Navigation setup and screens
│   │   │   │
│   │   │   ├── presentation/
│   │   │   │   ├── movie_details/  # Movie details screen
│   │   │   │   ├── movies_list/    # Movies list screen
│   │   │   │   ├── states/         # UI states
│   │   │   │   ├── theme/          # App theme
│   │   │   │   └── ui/             # Shared UI components
│   │   │   │
│   │   │   ├── utils/              # Utility classes and extensions
│   │   │   │
│   │   │   ├── App.kt              # Application class
│   │   │   └── MainActivity.kt     # Main activity
│   │   │
│   │   └── res/                    # Resources
│   │
│   └── test/                       # Unit tests
│
└── build.gradle.kts                # App level dependencies
```

## 🚀 Getting Started

### Prerequisites

- Android Studio Arctic Fox or higher
- JDK 11 or higher
- Android SDK 33+

### Setup

1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Run the app on an emulator or physical device

## License

This project is licensed under the MIT License - see the LICENSE file for details.
