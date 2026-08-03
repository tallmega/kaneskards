# Kane's Kards

A small Android flashcard app for early reading practice. Choose one of three levels, read each card, and move through the deck at your own pace.

## Levels

- **Level 1 — Short Sight Words:** common 3–4 letter words.
- **Level 2 — Medium Sight Words:** common 5–6 letter words.
- **Level 3 — Big Sight Words:** longer words with 7 or more letters.

## Change the cards

Edit [FlashcardData.kt](app/src/main/java/com/example/flashcards/FlashcardData.kt) for word decks or [SentenceData.kt](app/src/main/java/com/example/flashcards/SentenceData.kt) for the 100-sentence decks, then rebuild and reinstall the app. Each word level has a `wordCards` list, for example:

```kotlin
wordCards = listOf("cat", "dog", "sun").map { Flashcard(prompt = it) }
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
4. When it finishes, open the workflow run and download the `Kanes-Kards-release-apk` artifact.
5. Move the downloaded `app-release.apk` to your Android phone, open it, and install it.

Android will ask you to allow the browser or Files app to install apps from that source because this APK is not delivered through Google Play. You can revoke that permission afterwards.

## One-time release-signing setup

The workflow creates a signed release APK, so GitHub needs the private key as repository secrets. Generate this key once, keep a secure backup, and never commit or share the `.jks` file.

In PowerShell, run the following and choose a strong password when prompted. Use `kanes-kards` for the alias.

```powershell
keytool -genkeypair -v -keystore "$HOME\kanes-kards-release.jks" -alias kanes-kards -keyalg RSA -keysize 2048 -validity 10000
```

Then copy the key's Base64 value to your clipboard:

```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("$HOME\kanes-kards-release.jks")) | Set-Clipboard
```

In GitHub, open the repository's **Settings → Secrets and variables → Actions** and add these repository secrets:

| Secret | Value |
| --- | --- |
| `ANDROID_KEYSTORE_BASE64` | The Base64 value on your clipboard |
| `ANDROID_KEYSTORE_PASSWORD` | The keystore password you chose |
| `ANDROID_KEY_ALIAS` | `kanes-kards` |
| `ANDROID_KEY_PASSWORD` | The key password you chose |

Once those secrets are set, each successful workflow run provides a `Kanes-Kards-release-apk` artifact. After replacing the currently installed debug build once, later versions can be upgraded with `adb install -r`.
