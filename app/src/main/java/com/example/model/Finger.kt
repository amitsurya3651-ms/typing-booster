package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.FingerLeftIndex
import com.example.ui.theme.FingerLeftMiddle
import com.example.ui.theme.FingerLeftPinky
import com.example.ui.theme.FingerLeftRing
import com.example.ui.theme.FingerRightIndex
import com.example.ui.theme.FingerRightMiddle
import com.example.ui.theme.FingerRightPinky
import com.example.ui.theme.FingerRightRing
import com.example.ui.theme.FingerThumbs

enum class Hand(val titleEn: String, val titleHi: String) {
    LEFT("Left Hand", "बायां हाथ"),
    RIGHT("Right Hand", "दायां हाथ"),
    BOTH("Both Hands", "दोनों हाथ")
}

enum class Finger(
    val nameEn: String,
    val nameHi: String,
    val hand: Hand,
    val color: Color
) {
    LEFT_PINKY(
        nameEn = "Left Little Finger",
        nameHi = "बायां कनिष्ठिका उंगली (Little)",
        hand = Hand.LEFT,
        color = FingerLeftPinky
    ),
    LEFT_RING(
        nameEn = "Left Ring Finger",
        nameHi = "बायां अनामिका उंगली (Ring)",
        hand = Hand.LEFT,
        color = FingerLeftRing
    ),
    LEFT_MIDDLE(
        nameEn = "Left Middle Finger",
        nameHi = "बायां मध्यमा उंगली (Middle)",
        hand = Hand.LEFT,
        color = FingerLeftMiddle
    ),
    LEFT_INDEX(
        nameEn = "Left Index Finger",
        nameHi = "बायां तर्जनी उंगली (Index)",
        hand = Hand.LEFT,
        color = FingerLeftIndex
    ),
    RIGHT_INDEX(
        nameEn = "Right Index Finger",
        nameHi = "दायां तर्जनी उंगली (Index)",
        hand = Hand.RIGHT,
        color = FingerRightIndex
    ),
    RIGHT_MIDDLE(
        nameEn = "Right Middle Finger",
        nameHi = "दायां मध्यमा उंगली (Middle)",
        hand = Hand.RIGHT,
        color = FingerRightMiddle
    ),
    RIGHT_RING(
        nameEn = "Right Ring Finger",
        nameHi = "दायां अनामिका उंगली (Ring)",
        hand = Hand.RIGHT,
        color = FingerRightRing
    ),
    RIGHT_PINKY(
        nameEn = "Right Little Finger",
        nameHi = "दायां कनिष्ठिका उंगली (Little)",
        hand = Hand.RIGHT,
        color = FingerRightPinky
    ),
    THUMBS(
        nameEn = "Thumb (Spacebar)",
        nameHi = "अंगूठा (Spacebar)",
        hand = Hand.BOTH,
        color = FingerThumbs
    );

    companion object {
        fun getFingerForChar(ch: Char): Finger {
            return when (ch.lowercaseChar()) {
                '1', '`', '~', '!', 'q', 'a', 'z' -> LEFT_PINKY
                '2', '@', 'w', 's', 'x' -> LEFT_RING
                '3', '#', 'e', 'd', 'c' -> LEFT_MIDDLE
                '4', '$', '5', '%', 'r', 't', 'f', 'g', 'v', 'b' -> LEFT_INDEX
                '6', '^', '7', '&', 'y', 'u', 'h', 'j', 'n', 'm' -> RIGHT_INDEX
                '8', '*', 'i', 'k', ',', '<' -> RIGHT_MIDDLE
                '9', '(', 'o', 'l', '.', '>' -> RIGHT_RING
                '0', ')', '-', '_', '=', '+', 'p', '[', '{', ']', '}', '\\', '|', ';', ':', '\'', '"', '/', '?' -> RIGHT_PINKY
                ' ' -> THUMBS
                '\n' -> RIGHT_PINKY
                else -> THUMBS
            }
        }

        fun getFingerForKey(keyLabel: String): Finger {
            val upper = keyLabel.uppercase()
            return when {
                upper in listOf("1", "`", "~", "!", "Q", "A", "Z", "TAB", "CAPS", "LSHIFT") -> LEFT_PINKY
                upper in listOf("2", "@", "W", "S", "X") -> LEFT_RING
                upper in listOf("3", "#", "E", "D", "C") -> LEFT_MIDDLE
                upper in listOf("4", "$", "5", "%", "R", "T", "F", "G", "V", "B") -> LEFT_INDEX
                upper in listOf("6", "^", "7", "&", "Y", "U", "H", "J", "N", "M") -> RIGHT_INDEX
                upper in listOf("8", "*", "I", "K", ",", "<") -> RIGHT_MIDDLE
                upper in listOf("9", "(", "O", "L", ".", ">") -> RIGHT_RING
                upper in listOf("0", ")", "-", "_", "=", "+", "P", "[", "{", "]", "}", "\\", "|", ";", ":", "'", "\"", "/", "?", "ENTER", "BKSP", "RSHIFT") -> RIGHT_PINKY
                upper in listOf("SPACE", " ") -> THUMBS
                else -> THUMBS
            }
        }
    }
}
