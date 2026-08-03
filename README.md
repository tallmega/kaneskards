# Kanes Kards

A small Android flashcard app for early reading practice. Choose one of three levels, read each card, and move through the deck at your own pace.

## Levels

- **Level 1 — Short Sight Words:** common 3–4 letter words.
- **Level 2 — Medium Sight Words:** common 5–6 letter words.
- **Level 3 — Big Sight Words:** longer words with 7 or more letters.

## Change the cards

Edit [FlashcardData.kt](app/src/main/java/com/example/flashcards/FlashcardData.kt), then rebuild and reinstall the app. Each level has a `cards` list, for example:

```kotlin
cards = listOf("cat", "dog", "sun").map { Flashcard(prompt = it) }
```

Cards can also use an answer and a hint for future question/answer decks:

```kotlin
Flashcard(prompt = "What color is the sky?", answer = "Blue", hint = "Look up!")
```

## Build an APK without installing Android tools

This project includes a GitHub Actions workflow that builds the app on GitHub's servers. It is the easiest option when you do not want to install Android Studio, the Android SDK, Java, or Gradle locally.

1. Create a new repository on GitHub, then upload this project folder to it from the browser.
2. Open the repository's **Actions** tab and enable workflows if GitHub asks.
3. Select **Build Android APK** from the left sidebar and press **Run workflow**.
4. When it finishes, open the workflow run and download the `Kanes-Kards-debug-apk` artifact.
5. Move the downloaded `app-debug.apk` to your Android phone, open it, and install it.

Android will ask you to allow the browser or Files app to install apps from that source because this APK is not delivered through Google Play. You can revoke that permission afterwards.
