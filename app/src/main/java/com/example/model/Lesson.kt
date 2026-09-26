package com.example.model

enum class LessonCategory(val titleEn: String, val titleHi: String) {
    FOUNDATION("Foundation", "बुनियाद"),
    HOME_ROW("Home Row", "होम रो"),
    LEFT_HAND("Left Hand", "बायां हाथ"),
    RIGHT_HAND("Right Hand", "दायां हाथ"),
    COMPLETE_KEYBOARD("Complete Keyboard", "संपूर्ण कीबोर्ड"),
    WORDS("Words", "शब्द अभ्यास"),
    SENTENCES("Sentences", "वाक्य अभ्यास"),
    PARAGRAPHS("Paragraphs", "अनुच्छेद"),
    TIMED_TESTS("Timed Tests", "समयबद्ध टेस्ट"),
    EXAM_MASTERY("Mastery", "महारत")
}

data class Lesson(
    val id: Int,
    val stepNumber: Int,
    val titleEn: String,
    val titleHi: String,
    val subtitleEn: String,
    val subtitleHi: String,
    val explanationEn: String,
    val explanationHi: String,
    val category: LessonCategory,
    val practiceText: String,
    val targetWpm: Int = 15,
    val targetAccuracy: Int = 90,
    val defaultDurationSeconds: Int = 0 // 0 means untimed / complete text
)

object LessonCatalog {
    val lessons: List<Lesson> = listOf(
        Lesson(
            id = 1,
            stepNumber = 1,
            titleEn = "Lesson 1: Keyboard Introduction",
            titleHi = "पाठ 1: कीबोर्ड परिचय",
            subtitleEn = "Meet the QWERTY layout and control keys",
            subtitleHi = "QWERTY लेआउट और मुख्य कुंजियों को जानें",
            explanationEn = "Learn the 3 main rows and control keys: Space (thumb), Enter (new line), Backspace (erase), Shift (capitals), Caps Lock (uppercase lock), Tab (indent).",
            explanationHi = "3 मुख्य पंक्तियों और नियंत्रण कुंजियों को जानें: स्पेस (अंगूठा), एंटर (नई लाइन), बैकस्पेस (मिटाना), शिफ्ट (बड़े अक्षर), कैप्स लॉक, टैब।",
            category = LessonCategory.FOUNDATION,
            practiceText = "qwert yuiop asdfg hjkl zxcvb nm space enter tab shift",
            targetWpm = 10,
            targetAccuracy = 85
        ),
        Lesson(
            id = 2,
            stepNumber = 2,
            titleEn = "Lesson 2: Home Row Keys",
            titleHi = "पाठ 2: होम रो कुंजियाँ (Home Row)",
            subtitleEn = "Anchor fingers on ASDF and JKL;",
            subtitleHi = "ASDF और JKL; पर उंगलियों की मूल स्थिति",
            explanationEn = "Place your left fingers on A-S-D-F and right fingers on J-K-L-;. Feel the raised ridges on F and J with your index fingers to find position without looking!",
            explanationHi = "बाईं उंगलियों को A-S-D-F और दाईं उंगलियों को J-K-L-; पर रखें। बिना देखे स्थिति जानने के लिए F और J के उभार (Bumps) महसूस करें।",
            category = LessonCategory.HOME_ROW,
            practiceText = "asdf jkl; asdf jkl; fdsa ;lkj asdf jkl; aass ddff jjkk ll;; asdfjkl; fdsa;lkj",
            targetWpm = 15,
            targetAccuracy = 90
        ),
        Lesson(
            id = 3,
            stepNumber = 3,
            titleEn = "Lesson 3: Left Hand Keys",
            titleHi = "पाठ 3: बायां हाथ कुंजियाँ (Left Hand)",
            subtitleEn = "Master QWERT, ASDFG, and ZXCVB",
            subtitleHi = "QWERT, ASDFG और ZXCVB का अभ्यास",
            explanationEn = "Keep left hand fingers curved naturally. Little finger: Q, A, Z. Ring: W, S, X. Middle: E, D, C. Index: R, T, F, G, V, B.",
            explanationHi = "बाईं उंगलियों को मोड़कर रखें। कनिष्ठिका: Q,A,Z। अनामिका: W,S,X। मध्यमा: E,D,C। तर्जनी: R,T,F,G,V,B।",
            category = LessonCategory.LEFT_HAND,
            practiceText = "qwert asdfg zxcvb qwert asdfg zxcvb qaz wsx edc rfv tgb bva fds gtr ewa",
            targetWpm = 16,
            targetAccuracy = 90
        ),
        Lesson(
            id = 4,
            stepNumber = 4,
            titleEn = "Lesson 4: Right Hand Keys",
            titleHi = "पाठ 4: दायां हाथ कुंजियाँ (Right Hand)",
            subtitleEn = "Master YUIOP, HJKL, and NM",
            subtitleHi = "YUIOP, HJKL और NM का अभ्यास",
            explanationEn = "Index finger: Y, U, H, J, N, M. Middle: I, K, comma. Ring: O, L, period. Little: P, semicolon, slash.",
            explanationHi = "तर्जनी: Y,U,H,J,N,M। मध्यमा: I,K,अल्पविराम। अनामिका: O,L,पूर्णविराम। कनिष्ठिका: P,सेमीकोलन।",
            category = LessonCategory.RIGHT_HAND,
            practiceText = "yuiop hjkl nm yuiop hjkl nm yhn ujm ikl olp jnh mju kol plm hin yun",
            targetWpm = 18,
            targetAccuracy = 90
        ),
        Lesson(
            id = 5,
            stepNumber = 5,
            titleEn = "Lesson 5: Complete Keyboard Practice",
            titleHi = "पाठ 5: संपूर्ण कीबोर्ड अभ्यास",
            subtitleEn = "Mix all rows and coordinate both hands",
            subtitleHi = "सभी पंक्तियों और दोनों हाथों का समन्वय",
            explanationEn = "Harmonize both hands across all three rows. Return your fingers back to the home row after every stroke.",
            explanationHi = "दोनों हाथों का तालमेल बनाएं। प्रत्येक की दबाने के बाद उंगलियों को होम रो पर वापस लाएं।",
            category = LessonCategory.COMPLETE_KEYBOARD,
            practiceText = "the quick brown fox jumps over the lazy dog pack my box with five dozen liquor jugs",
            targetWpm = 20,
            targetAccuracy = 92
        ),
        Lesson(
            id = 6,
            stepNumber = 6,
            titleEn = "Lesson 6: Simple Words",
            titleHi = "पाठ 6: सरल शब्द अभ्यास",
            subtitleEn = "Type common English words smoothly",
            subtitleHi = "रोजमर्रा के सामान्य शब्दों की टाइपिंग",
            explanationEn = "Type whole words as continuous rhythmic units rather than individual disjointed letters.",
            explanationHi = "शब्दों को एक लय में टाइप करें, एक-एक अक्षर सोचने की बजाय पूरे शब्द पर ध्यान दें।",
            category = LessonCategory.WORDS,
            practiceText = "sad dad ask fall flag lad milk book school all flash glad road star time note work page life hero blue clear mind test jump quick hand",
            targetWpm = 22,
            targetAccuracy = 92
        ),
        Lesson(
            id = 7,
            stepNumber = 7,
            titleEn = "Lesson 7: Sentences & Punctuation",
            titleHi = "पाठ 7: वाक्य और विराम चिह्न",
            subtitleEn = "Type complete sentences with capitals and periods",
            subtitleHi = "बड़े अक्षरों और पूर्ण विराम के साथ वाक्य",
            explanationEn = "Use the opposite pinky finger to hold Shift for capitals: left shift for right-hand keys, right shift for left-hand keys.",
            explanationHi = "कैपिटल अक्षर के लिए विपरीत हाथ की छोटी उंगली से Shift दबाएं।",
            category = LessonCategory.SENTENCES,
            practiceText = "I am learning typing. Typing is a useful skill. Practice every day. Fast fingers make light work. Clear minds build great focus and precision.",
            targetWpm = 25,
            targetAccuracy = 94
        ),
        Lesson(
            id = 8,
            stepNumber = 8,
            titleEn = "Lesson 8: Full Paragraphs",
            titleHi = "पाठ 8: पूर्ण अनुच्छेद (Paragraphs)",
            subtitleEn = "Build stamina and rhythm with longer passages",
            subtitleHi = "लंबे अनुच्छेदों के साथ निरंतरता बनाएं",
            explanationEn = "Keep a relaxed, steady cadence. Do not rush into errors; speed naturally follows precision.",
            explanationHi = "शांत और स्थिर गति बनाए रखें। गलती न करें; सटीकता आने पर गति अपने आप बढ़ जाएगी।",
            category = LessonCategory.PARAGRAPHS,
            practiceText = "Touch typing allows you to type without looking at the keyboard. Your fingers remember the exact position of every key through consistent muscle memory. By practicing a few minutes every morning, your typing speed and accuracy will improve dramatically.",
            targetWpm = 28,
            targetAccuracy = 95
        ),
        Lesson(
            id = 9,
            stepNumber = 9,
            titleEn = "Lesson 9: Timed Speed Practice",
            titleHi = "पाठ 9: समयबद्ध गति अभ्यास",
            subtitleEn = "Practice with 1, 2, or 5 minute countdown",
            subtitleHi = "1, 2, या 5 मिनट के टाइमर के साथ अभ्यास",
            explanationEn = "Test your speed against the clock. The timer will automatically submit and calculate your Gross and Net WPM.",
            explanationHi = "घड़ी के विरुद्ध अपनी गति का परीक्षण करें। टाइमर समाप्त होने पर आपका Gross और Net WPM स्वतः दिखेगा।",
            category = LessonCategory.TIMED_TESTS,
            practiceText = "Consistent daily typing practice is the most effective way to excel in exams and office work. Good posture with your back straight and wrists floating slightly above the desk prevents fatigue and allows your hands to glide across keys effortlessly.",
            targetWpm = 30,
            targetAccuracy = 95,
            defaultDurationSeconds = 60
        ),
        Lesson(
            id = 10,
            stepNumber = 10,
            titleEn = "Lesson 10: 10-Minute Typing Test",
            titleHi = "पाठ 10: 10-मिनट टाइपिंग परीक्षा",
            subtitleEn = "Full official competitive-exam standard test",
            subtitleHi = "सरकारी व प्रतियोगी परीक्षा मानक 10-मिनट टेस्ट",
            explanationEn = "Official 10-minute exam simulation. Keep typing steadily through the entire duration until the timer reaches zero.",
            explanationHi = "10 मिनट की संपूर्ण परीक्षा। समय समाप्त होने तक लगातार एकाग्रता से टाइप करें।",
            category = LessonCategory.TIMED_TESTS,
            practiceText = "In the modern digital era, typing speed is an essential qualification for competitive examinations and professional success. Whether you are preparing for government typing skill tests, writing reports, coding applications, or communicating in business, mastering touch typing saves hundreds of productive hours every year. Touch typing relies entirely on tactile muscle memory rather than sight. The ten fingers rest gently on the home row keys, with the left index finger on F and the right index finger on J. These two anchor keys feature raised bumps that guide your hands back to their base positions without ever glancing down at the physical keyboard. When you encounter a challenging key, pause for a microsecond rather than striking the wrong letter. Accuracy is the mother of speed. Students who concentrate on typing correctly develop flawless subconscious reflexes. Over weeks of disciplined training, complex letter patterns and vocabulary flow directly from thought onto the screen with zero friction. Keep your posture upright, breathe calmly, and let your fingers find their natural rhythm.",
            targetWpm = 35,
            targetAccuracy = 95,
            defaultDurationSeconds = 600
        ),
        Lesson(
            id = 11,
            stepNumber = 11,
            titleEn = "Lesson 11: Weak-Key Practice",
            titleHi = "पाठ 11: कमजोर कुंजियों का सुधार",
            subtitleEn = "Auto-generated drills targeting your problem keys",
            subtitleHi = "गलत होने वाली कुंजियों के लिए विशेष अभ्यास",
            explanationEn = "Target your lowest accuracy keys with custom drill exercises designed specifically to eliminate recurring mistakes.",
            explanationHi = "जिन कुंजियों में सबसे ज्यादा गलतियां होती हैं, उनके विशेष अभ्यासों द्वारा अपनी गलतियों को दूर करें।",
            category = LessonCategory.EXAM_MASTERY,
            practiceText = "dad did add sad dark desk address danger loud door red run road dark learn word write king keep make take book think",
            targetWpm = 25,
            targetAccuracy = 95
        ),
        Lesson(
            id = 12,
            stepNumber = 12,
            titleEn = "Lesson 12: Progress & Mastery Tracking",
            titleHi = "पाठ 12: प्रगति समीक्षा और महारत",
            subtitleEn = "Review your speed milestones and streaks",
            subtitleHi = "अपनी गति, सटीकता और स्ट्रीक का विश्लेषण",
            explanationEn = "Check your WPM growth curves, unlock speed achievements, and maintain your daily typing streak!",
            explanationHi = "अपने WPM ग्राफ को देखें, उपलब्धियां (Achievements) अनलॉक करें और रोज़ाना अभ्यास जारी रखें!",
            category = LessonCategory.EXAM_MASTERY,
            practiceText = "Congratulations on advancing through the Typing Master curriculum! Regular practice will turn you into an effortless touch typist capable of exceeding fifty words per minute with ease.",
            targetWpm = 40,
            targetAccuracy = 96
        )
    )

    fun getLessonById(id: Int): Lesson {
        return lessons.find { it.id == id } ?: lessons.first()
    }
}
