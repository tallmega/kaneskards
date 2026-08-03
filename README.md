# Kane's Kards

Kane's Kards is a small, offline Android reading-practice app. It is designed for short kindergarten-friendly practice rounds and does not use ads, accounts, or network services in the app.

## What it does

- Provides three progressive reading levels:
  - **Level 1:** short sight words and early-reader sentences using words of four letters or fewer.
  - **Level 2:** medium sight words and sentences using vocabulary from Levels 1–2, up to six letters.
  - **Level 3:** longer sight words and more challenging sentences using all earlier vocabulary.
- Lets the child choose **Words** or **Sentences** after selecting a level.
- Includes 100 shuffled word cards and 100 shuffled sentence cards in every level.
- Uses a configurable practice round of **5–50 cards**, in steps of five. The default is **10 cards** and the choice is saved on the device.
- Uses **Got it!** and **Try Again** buttons. Missed cards form a review round and repeat until marked correct.
- Shows a fireworks celebration after all cards in the round have been completed.
- Keeps the active deck, round, card, and review queue when the phone rotates.

## Using the app

1. On the home screen, open **Settings** to choose the number of cards per practice round.
2. Choose a level, then choose **Words** or **Sentences**.
3. Read the card aloud and select **Got it!** or **Try Again**.
4. Complete any review cards to finish the round.
5. On the celebration screen, choose **Play another** for a fresh shuffled round or return to **Levels**.

## Updating decks

- Word decks and level metadata are in [FlashcardData.kt](app/src/main/java/com/example/flashcards/FlashcardData.kt).
- The 100 sentence cards for each level are in [SentenceData.kt](app/src/main/java/com/example/flashcards/SentenceData.kt).

Word cards use the following format:

```kotlin
wordCards = listOf("cat", "dog", "sun").map { Flashcard(prompt = it) }
```

Sentence cards are plain strings:

```kotlin
val levelOne = listOf("I see a cat.", "The cat can run.")
```

Keep each sentence list at 100 cards and preserve the intended vocabulary progression when editing.

## Build a signed APK with GitHub Actions

The repository includes a GitHub Actions workflow that builds a signed release APK on GitHub's servers. No Android Studio, Android SDK, or local Gradle setup is required.

### One-time signing setup

The local signing key is intentionally excluded from Git. It lives at `signing/kanes-kards-release.jks`, and the matching values are stored in the ignored `.env` file. Keep secure backups of both files; losing the key prevents future APKs from upgrading the installed app.

In GitHub, open **Settings → Secrets and variables → Actions** and add the following repository secrets. Copy each value from the matching line in the local `.env` file.

| GitHub secret | Local `.env` value |
| --- | --- |
| `ANDROID_KEYSTORE_BASE64` | `ANDROID_KEYSTORE_BASE64` |
| `ANDROID_KEYSTORE_PASSWORD` | `ANDROID_KEYSTORE_PASSWORD` |
| `ANDROID_KEY_ALIAS` | `ANDROID_KEY_ALIAS` |
| `ANDROID_KEY_PASSWORD` | `ANDROID_KEY_PASSWORD` |

Never commit, email, or share `.env`, the `.jks` key, or their contents.

### Create and download a build

1. Commit and push the tracked project changes to the `main` branch.
2. Open the repository's **Actions** tab and select **Build Android APK**. A push starts it automatically; it can also be run manually.
3. When the build succeeds, download the `Kanes-Kards-release-apk` artifact.
4. Extract the artifact and use `app-release.apk` to install the app.

## Install and update with ADB

Connect an Android phone with USB debugging enabled, then use the Android SDK Platform-Tools from a Windows PowerShell prompt.

The first signed release replaces the previous debug build, so remove the debug version once:

```powershell
.\adb.exe uninstall com.kaneskards.app
.\adb.exe install "C:\path\to\app-release.apk"
```

Later signed releases use the same key and can upgrade in place, preserving app data:

```powershell
.\adb.exe install -r "C:\path\to\app-release.apk"
```
