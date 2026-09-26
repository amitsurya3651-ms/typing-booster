package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.TypedCharState
import com.example.ui.theme.TypingCorrect
import com.example.ui.theme.TypingCursor
import com.example.ui.theme.TypingIncorrect

@Composable
fun PracticeTextView(
    targetText: String,
    currentIndex: Int,
    charStates: List<TypedCharState>,
    isHindi: Boolean,
    focusRequester: FocusRequester,
    onPhysicalChar: (Char) -> Unit,
    onBackspace: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Auto-scroll as user progresses
    LaunchedEffect(currentIndex) {
        if (targetText.isNotEmpty()) {
            val progressRatio = currentIndex.toFloat() / targetText.length
            val targetScroll = (scrollState.maxValue * progressRatio).toInt()
            scrollState.animateScrollTo(targetScroll)
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "cursor")
    val cursorAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cursorBlink"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("practice_text_view"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            val annotatedText = buildAnnotatedString {
                for (i in targetText.indices) {
                    val ch = targetText[i]
                    val displayChar = if (ch == '\n') " ⏎\n" else ch.toString()

                    when {
                        i < currentIndex -> {
                            val state = charStates.getOrNull(i) ?: TypedCharState.Pending
                            if (state is TypedCharState.Correct) {
                                withStyle(
                                    SpanStyle(
                                        color = TypingCorrect,
                                        fontWeight = FontWeight.Medium
                                    )
                                ) {
                                    append(displayChar)
                                }
                            } else {
                                withStyle(
                                    SpanStyle(
                                        color = TypingIncorrect,
                                        fontWeight = FontWeight.Bold,
                                        textDecoration = TextDecoration.Underline,
                                        background = TypingIncorrect.copy(alpha = 0.15f)
                                    )
                                ) {
                                    append(displayChar)
                                }
                            }
                        }
                        i == currentIndex -> {
                            // Target cursor char
                            withStyle(
                                SpanStyle(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    background = TypingCursor.copy(alpha = cursorAlpha * 0.45f),
                                    fontWeight = FontWeight.ExtraBold,
                                    textDecoration = TextDecoration.Underline
                                )
                            ) {
                                append(displayChar)
                            }
                        }
                        else -> {
                            // Upcoming text
                            withStyle(
                                SpanStyle(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                    fontWeight = FontWeight.Normal
                                )
                            ) {
                                append(displayChar)
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = annotatedText,
                    fontSize = 18.sp,
                    lineHeight = 28.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
