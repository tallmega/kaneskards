package com.kaneskards.app

/**
 * The app's word-card data lives here. Sentence cards are in [SentenceData].
 * [answer] and [hint] are optional so these can later become question/answer cards.
 */
data class Flashcard(
    val prompt: String,
    val answer: String? = null,
    val hint: String? = null,
)

enum class DeckType(val title: String, val description: String) {
    Words(title = "Words", description = "Practice single sight words."),
    Sentences(title = "Sentences", description = "Read short sentences."),
    IrregularWords(title = "Irregular Words", description = "Practice words with tricky spellings."),
}

data class CardLevel(
    val number: Int,
    val title: String,
    val description: String,
    val wordCards: List<Flashcard>,
    val sentenceCards: List<Flashcard>,
    val irregularWordCards: List<Flashcard>,
) {
    fun cardsFor(deckType: DeckType): List<Flashcard> = when (deckType) {
        DeckType.Words -> wordCards
        DeckType.Sentences -> sentenceCards
        DeckType.IrregularWords -> irregularWordCards
    }
}

object FlashcardData {
    val levels = listOf(
        CardLevel(
            number = 1,
            title = "Short Sight Words",
            description = "Read common 2–4 letter words.",
            wordCards = cardsForWords(listOf(
                "the", "and", "for", "you", "are", "not", "with", "have", "this", "from",
                "they", "said", "what", "when", "were", "your", "come", "some", "want",
                "like", "look", "make", "play", "help", "here", "away", "over", "give", "live",
                "many", "much", "only", "once", "upon", "them", "then", "well", "will", "just",
                "know", "take", "tell", "work", "call", "does", "done", "goes", "read",
                "yes", "yet", "also", "all", "any", "ask", "bad", "big", "boy", "but", "can",
                "day", "did", "eat", "end", "far", "few", "get", "got", "had", "has",
                "her", "him", "his", "how", "into", "its", "let", "may", "new", "now",
                "off", "old", "one", "our", "out", "own", "put", "say", "she", "show",
                "soon", "than", "that", "too", "try", "use", "very", "walk", "wash", "who", "wish",
            ) + IrregularWordData.levelOneWords),
            sentenceCards = SentenceData.levelOne.map { Flashcard(prompt = it) },
            irregularWordCards = cardsForWords(IrregularWordData.levelOneWords),
        ),
        CardLevel(
            number = 2,
            title = "Medium Sight Words",
            description = "Read common 5–6 letter words.",
            wordCards = cardsForWords(listOf(
                "about", "after", "again", "could", "every", "first", "found", "great", "house", "large",
                "learn", "other", "place", "right", "small", "still", "their", "these", "three", "where",
                "which", "while", "world", "would", "write", "bring", "carry", "clean", "close", "drink",
                "eight", "family", "happy", "laugh", "light", "money", "mother", "never", "paper", "people",
                "quiet", "round", "should", "sleep", "story", "thank", "think", "under", "water", "young",
                "always", "around", "before", "better", "change", "choose", "circle", "colour", "cousin", "dinner",
                "during", "early", "enough", "eleven", "enjoy", "follow", "friend", "funny", "gentle", "heard",
                "inside", "itself", "maybe", "minute", "monday", "number", "orange", "please", "pretty", "purple",
                "queen", "river", "season", "second", "seven", "smile", "spring", "summer", "things", "today",
                "winter", "woman", "yellow", "zebra", "across", "animal", "answer", "become", "behind", "below",
            ) + IrregularWordData.levelTwoWords),
            sentenceCards = SentenceData.levelTwo.map { Flashcard(prompt = it) },
            irregularWordCards = cardsForWords(IrregularWordData.levelTwoWords),
        ),
        CardLevel(
            number = 3,
            title = "Big Sight Words",
            description = "Read longer 7+ letter words.",
            wordCards = cardsForWords(listOf(
                "because", "another", "different", "important", "children", "beautiful", "together", "remember",
                "something", "favorite", "everywhere", "afternoon", "interesting", "sometimes", "everyone", "tomorrow",
                "anything", "answering", "birthday", "building", "brother", "catching", "chapter", "climbing",
                "complete", "country", "dancing", "discuss", "example", "excited", "exercise", "finally",
                "finished", "happened", "holiday", "instead", "january", "kitchen", "library", "morning",
                "nothing", "outside", "picture", "problem", "probably", "question", "reading", "school",
                "special", "teacher",
                "account", "airport", "already", "anybody", "arriving", "bicycle", "blanket", "careful", "certain", "clothes",
                "crowded", "daughter", "decided", "describe", "diamond", "electric", "enjoying", "evening", "exciting", "explained",
                "feeling", "forward", "friends", "frightened", "getting", "grandma", "happily", "himself", "history", "hundred",
                "journey", "language", "leaving", "listening", "million", "mountain", "parents", "playground", "possible", "practice",
                "present", "rainbow", "station", "straight", "surprise", "swimming", "tuesday", "usually", "village", "weekdays",
            ) + IrregularWordData.levelThreeWords),
            sentenceCards = SentenceData.levelThree.map { Flashcard(prompt = it) },
            irregularWordCards = cardsForWords(IrregularWordData.levelThreeWords),
        ),
    )

    private fun cardsForWords(words: List<String>): List<Flashcard> =
        words.distinct().map { Flashcard(prompt = it) }
}
