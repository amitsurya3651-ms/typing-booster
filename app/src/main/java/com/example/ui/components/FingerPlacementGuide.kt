package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Finger
import com.example.model.Hand

@Composable
fun FingerPlacementGuide(
    targetChar: Char?,
    language: String = "en",
    showLegend: Boolean = false,
    modifier: Modifier = Modifier
) {
    val isHindi = language == "hi"
    val targetFinger = targetChar?.let { Finger.getFingerForChar(it) } ?: Finger.THUMBS
    val targetKeyDisplay = when (targetChar) {
        ' ' -> if (isHindi) "स्पेस (SPACE)" else "SPACE [ ␣ ]"
        '\n' -> if (isHindi) "एंटर (ENTER)" else "ENTER [ ⏎ ]"
        null -> "-"
        else -> targetChar.toString()
    }

    val handText = when (targetFinger.hand) {
        Hand.LEFT -> if (isHindi) "बायां हाथ (Left Hand)" else "Left Hand"
        Hand.RIGHT -> if (isHindi) "दायां हाथ (Right Hand)" else "Right Hand"
        Hand.BOTH -> if (isHindi) "अंगूठा (Thumbs)" else "Thumbs / Space"
    }

    val fingerName = if (isHindi) targetFinger.nameHi else targetFinger.nameEn

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("finger_placement_guide"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Target Key Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(targetFinger.color.copy(alpha = 0.2f))
                            .border(2.dp, targetFinger.color, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (targetKeyDisplay.length > 2) targetKeyDisplay.take(3) else targetKeyDisplay,
                            fontSize = if (targetKeyDisplay.length > 2) 13.sp else 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = targetFinger.color
                        )
                    }

                    Column {
                        Text(
                            text = if (isHindi) "लक्ष्य कुंजी (TARGET)" else "NEXT TARGET KEY",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = targetKeyDisplay,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Right: Target Finger & Hand Pill
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = targetFinger.color.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, targetFinger.color)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(targetFinger.color)
                        )
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = handText,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = fingerName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Visual Hands Strip showing all 8 fingers + space
            Spacer(modifier = Modifier.height(10.dp))
            HandsVisualizer(activeFinger = targetFinger, isHindi = isHindi)

            // Permanent Color Legend if expanded
            if (showLegend) {
                Spacer(modifier = Modifier.height(10.dp))
                FingerColorLegend(isHindi = isHindi)
            }
        }
    }
}

@Composable
fun HandsVisualizer(
    activeFinger: Finger,
    isHindi: Boolean,
    modifier: Modifier = Modifier
) {
    val fingersLeft = listOf(
        Finger.LEFT_PINKY to "Pinky",
        Finger.LEFT_RING to "Ring",
        Finger.LEFT_MIDDLE to "Mid",
        Finger.LEFT_INDEX to "Index"
    )
    val fingersRight = listOf(
        Finger.RIGHT_INDEX to "Index",
        Finger.RIGHT_MIDDLE to "Mid",
        Finger.RIGHT_RING to "Ring",
        Finger.RIGHT_PINKY to "Pinky"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left hand group
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            fingersLeft.forEach { (finger, label) ->
                FingerMiniPill(
                    finger = finger,
                    label = label,
                    isActive = activeFinger == finger
                )
            }
        }

        // Thumb / Space in middle
        FingerMiniPill(
            finger = Finger.THUMBS,
            label = "Thumb",
            isActive = activeFinger == Finger.THUMBS
        )

        // Right hand group
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            fingersRight.forEach { (finger, label) ->
                FingerMiniPill(
                    finger = finger,
                    label = label,
                    isActive = activeFinger == finger
                )
            }
        }
    }
}

@Composable
private fun FingerMiniPill(
    finger: Finger,
    label: String,
    isActive: Boolean
) {
    val background by animateColorAsState(
        targetValue = if (isActive) finger.color else finger.color.copy(alpha = 0.25f),
        label = "miniPillBg"
    )

    Box(
        modifier = Modifier
            .size(width = 30.dp, height = if (isActive) 24.dp else 18.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(background)
            .border(
                width = if (isActive) 1.5.dp else 0.5.dp,
                color = if (isActive) MaterialTheme.colorScheme.onSurface else finger.color,
                shape = RoundedCornerShape(4.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label.take(1),
            fontSize = 10.sp,
            fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.Normal,
            color = if (isActive) Color.White else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun FingerColorLegend(isHindi: Boolean, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
            .padding(8.dp)
    ) {
        Text(
            text = if (isHindi) "उंगली रंग निर्देशिका (Finger Color Legend):" else "Finger Color Legend:",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))

        val legendItems = listOf(
            Triple(Finger.LEFT_PINKY, "Left Pinky (A, Q, Z)", "बायां कनिष्ठिका (A, Q, Z)"),
            Triple(Finger.LEFT_RING, "Left Ring (S, W, X)", "बायां अनामिका (S, W, X)"),
            Triple(Finger.LEFT_MIDDLE, "Left Middle (D, E, C)", "बायां मध्यमा (D, E, C)"),
            Triple(Finger.LEFT_INDEX, "Left Index (F, R, V, G, T, B)", "बायां तर्जनी (F, R, V, G, T, B)"),
            Triple(Finger.RIGHT_INDEX, "Right Index (J, U, M, H, Y, N)", "दायां तर्जनी (J, U, M, H, Y, N)"),
            Triple(Finger.RIGHT_MIDDLE, "Right Middle (K, I, ,)", "दायां मध्यमा (K, I, ,)"),
            Triple(Finger.RIGHT_RING, "Right Ring (L, O, .)", "दायां अनामिका (L, O, .)"),
            Triple(Finger.RIGHT_PINKY, "Right Pinky (;, P, /)", "दायां कनिष्ठिका (;, P, /)"),
            Triple(Finger.THUMBS, "Thumbs (Spacebar)", "अंगूठा (स्पेसबार)")
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                legendItems.take(5).forEach { (finger, en, hi) ->
                    LegendRow(finger.color, if (isHindi) hi else en)
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                legendItems.drop(5).forEach { (finger, en, hi) ->
                    LegendRow(finger.color, if (isHindi) hi else en)
                }
            }
        }
    }
}

@Composable
private fun LegendRow(color: Color, label: String) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
