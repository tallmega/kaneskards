# Kane's Kards

Kane's Kards is a small, offline Android reading-practice app. It is designed for short kindergarten-friendly practice rounds and does not use ads, accounts, or network services in the app.

## What it does

- Provides three progressive reading levels:
  - **Level 1:** short sight words and early-reader sentences using words of two to four letters.
  - **Level 2:** medium sight words and sentences using vocabulary from Levels 1–2, up to six letters.
  - **Level 3:** longer sight words and more challenging sentences using all earlier vocabulary.
- Lets the child choose **Words**, **Sentences**, or **Irregular Words** after selecting a level.
- Includes at least 100 shuffled word cards and 100 shuffled sentence cards in every level. The Irregular Words deck offers focused practice with common tricky spellings, and those words are also available in the normal Words decks.
- The Irregular Words deck includes plain-word entries from UFLI's *Printable Cards for Irregular “Heart” Words*. Kane's Kards is independent and is not affiliated with, endorsed by, or sponsored by UFLI or the University of Florida.
- Uses a configurable practice round of **5–50 cards**, in steps of five. The default is **10 cards** and the choice is saved on the device.
- Uses **Got it!** and **Try Again** buttons. Missed cards form a review round and repeat until marked correct.
- Shows a fireworks celebration after all cards in the round have been completed.
- Keeps the active deck, round, card, and review queue when the phone rotates.

## Using the app

1. On the home screen, open **Settings** to choose the number of cards per practice round.
2. Choose a level, then choose **Words**, **Sentences**, or **Irregular Words**.
3. Read the card aloud and select **Got it!** or **Try Again**.
4. Complete any review cards to finish the round.
5. On the celebration screen, choose **Play another** for a fresh shuffled round or return to **Levels**.

## Updating decks

- Word decks and level metadata are in [FlashcardData.kt](app/src/main/java/com/example/flashcards/FlashcardData.kt). The dedicated irregular-word lists are in [IrregularWordData.kt](app/src/main/java/com/example/flashcards/IrregularWordData.kt).
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

