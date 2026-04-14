# Pokedex App

`Pokedex App` is a native Android app built with Kotlin, Jetpack Compose, and the Navigation component. It lets users search for a Pokemon by name, view detailed information from the [PokeAPI](https://pokeapi.co/), and create a simple camera composite by overlaying the selected Pokemon's sprite onto a captured photo.

## Features

- Search for Pokemon by name
- Fetch Pokemon details and species data from PokeAPI
- View artwork, types, flavor text, and abilities
- Navigate through a simple multi-screen flow:
  - Search
  - Detail
  - Camera
- Remember the last successful Pokemon search locally
- Capture a photo and save a sprite-overlay composite to the device gallery

## Screens

- `Search`: enter a Pokemon name and submit a lookup
- `Detail`: view the selected Pokemon's artwork, type badges, description, and abilities
- `Camera`: launch the device camera and save a photo with the current Pokemon sprite overlaid

Example screenshots captured from the emulator are available in [`artifacts/screenshots`](./artifacts/screenshots).

## Tech Stack

- Kotlin
- Android SDK 36
- Jetpack Compose
- AndroidX Navigation
- Retrofit
- Moshi
- Coil
- SharedPreferences

## Project Structure

```text
app/src/main/java/com/example/pokedexapp/
├── MainActivity.kt
├── PokemonSearchFragment.kt
├── PokemonDetailFragment.kt
├── PokemonCameraFragment.kt
├── PokemonCameraScreen.kt
├── CameraFeature.kt
├── data/
│   ├── PokeApiService.kt
│   ├── PokemonRepository.kt
│   ├── Pokemon.kt
│   ├── PokemonSpecies.kt
│   ├── LoadingStatus.kt
│   └── UserPreferencesRepository.kt
└── ui/
    ├── PokemonViewModel.kt
    ├── home/HomeScreen.kt
    ├── detail/DetailScreen.kt
    └── theme/
```

## How It Works

1. The user enters a Pokemon name on the home screen.
2. `PokemonViewModel` requests both:
   - `/pokemon/{name}`
   - `/pokemon-species/{name}`
3. `PokemonRepository` returns the results as `Result` objects.
4. On success, the app stores the last search in `SharedPreferences` and navigates to the detail screen.
5. From the detail screen, the user can open the camera flow and create a saved composite image.

## Requirements

- Android Studio
- Android SDK installed
- JDK 11
- An Android emulator or physical Android device
- Internet access for PokeAPI requests

## Running the Project in Android Studio

1. Open the project folder in Android Studio.
2. Let Gradle sync finish.
3. Start an emulator or connect an Android device.
4. Click `Run` on the `app` configuration.

## Running from the Command Line

Build the debug APK:

```bash
./gradlew assembleDebug
```

Install it on a connected device or emulator:

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

Launch the app:

```bash
adb shell am start -n com.example.pokedexapp/.MainActivity
```

## Permissions

The app requests:

- `INTERNET` for PokeAPI requests
- `CAMERA` for photo capture
- `WRITE_EXTERNAL_STORAGE` on older Android versions for saving images

## Notes

- The app restores the last successful search when reopened.
- Camera behavior depends on emulator or device camera support.
- The composite image is saved to the device gallery under `Pictures/PokedexApp` on supported Android versions.

## Future Improvements

- Add favorites or a search history screen
- Improve error handling and loading feedback
- Add automated UI tests
- Support more Pokemon data such as stats, height, weight, and evolution chains
- Refine the camera composite layout and scaling

## License

This project was created for a course final project. Add a license here if you plan to distribute or reuse it outside the course context.
