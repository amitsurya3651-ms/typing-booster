package com.example.model

data class KeyAccuracy(
    val char: Char,
    val attempts: Int,
    val errors: Int,
    val accuracyPercent: Int
)

object WeakKeyHelper {
    // Word banks rich in specific letters for personalized drilling
    private val keyWordBank: Map<Char, List<String>> = mapOf(
        'a' to listOf("all", "and", "ask", "also", "area", "away", "about", "again", "alarm", "awake"),
        'b' to listOf("book", "back", "blue", "best", "bird", "baby", "build", "brave", "bridge", "bright"),
        'c' to listOf("call", "city", "code", "cool", "clean", "clock", "cloud", "catch", "create", "circle"),
        'd' to listOf("dad", "did", "add", "sad", "dark", "desk", "door", "danger", "loud", "rider", "modern", "address"),
        'e' to listOf("ever", "even", "easy", "eyes", "enter", "every", "eagle", "effort", "energy", "expert"),
        'f' to listOf("fall", "flag", "fast", "four", "free", "fire", "first", "focus", "finger", "future"),
        'g' to listOf("good", "give", "gold", "game", "grow", "glow", "great", "glass", "guard", "guide"),
        'h' to listOf("hand", "help", "home", "hope", "hard", "high", "heart", "heavy", "honor", "human"),
        'i' to listOf("into", "item", "iron", "idea", "image", "input", "inner", "issue", "island", "impact"),
        'j' to listOf("jump", "join", "just", "judge", "joy", "juice", "jelly", "journey", "junior", "jacket"),
        'k' to listOf("kid", "key", "kiss", "kick", "keep", "look", "make", "take", "book", "think", "skill", "quick"),
        'l' to listOf("lad", "like", "line", "look", "life", "live", "light", "learn", "level", "limit"),
        'm' to listOf("milk", "make", "more", "mind", "move", "much", "music", "magic", "master", "minute"),
        'n' to listOf("name", "near", "need", "next", "nice", "note", "never", "night", "number", "nature"),
        'o' to listOf("open", "once", "only", "over", "order", "ocean", "offer", "other", "origin", "option"),
        'p' to listOf("page", "path", "play", "post", "part", "pure", "paper", "peace", "point", "practice"),
        'q' to listOf("quick", "quite", "queen", "quiet", "quote", "query", "quest", "qualify", "quality"),
        'r' to listOf("are", "red", "run", "try", "dark", "road", "learn", "word", "write", "ready", "river", "train"),
        's' to listOf("sad", "see", "sun", "star", "stay", "step", "speed", "sound", "smart", "system"),
        't' to listOf("time", "take", "team", "tell", "true", "type", "today", "total", "table", "target"),
        'u' to listOf("unit", "user", "upon", "under", "urban", "usual", "useful", "unique", "update", "urgent"),
        'v' to listOf("view", "very", "vast", "vote", "voice", "visit", "value", "vivid", "vector", "vision"),
        'w' to listOf("work", "word", "with", "walk", "warm", "wave", "water", "world", "write", "winner"),
        'x' to listOf("text", "next", "exam", "exit", "axis", "extra", "exact", "expect", "expert", "matrix"),
        'y' to listOf("year", "your", "yes", "yard", "young", "yield", "yellow", "yesterday", "system"),
        'z' to listOf("zone", "zero", "zoom", "zeal", "zebra", "puzzle", "breeze", "freeze", "blizzard")
    )

    fun generateDrillForWeakKeys(weakKeys: List<Char>, wordCount: Int = 25): String {
        if (weakKeys.isEmpty()) {
            return "focus accuracy speed practice typing master clean rhythm effortless precision"
        }
        val selectedWords = mutableListOf<String>()
        val letters = weakKeys.map { it.lowercaseChar() }

        while (selectedWords.size < wordCount) {
            for (char in letters) {
                val words = keyWordBank[char] ?: listOf(char.toString().repeat(4))
                val randomWord = words.random()
                selectedWords.add(randomWord)
                if (selectedWords.size >= wordCount) break
            }
        }
        return selectedWords.shuffled().joinToString(" ")
    }

    fun generateDrillForSingleKey(char: Char): String {
        val lower = char.lowercaseChar()
        val words = keyWordBank[lower] ?: listOf("$lower$lower", "$lower $lower")
        val pattern = listOf(
            "$lower $lower $lower",
            "$lower$lower $lower$lower",
            words.shuffled().take(6).joinToString(" "),
            "$lower$lower$lower $lower$lower$lower",
            words.shuffled().take(6).joinToString(" ")
        )
        return pattern.joinToString(" ")
    }
}
