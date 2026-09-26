package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Finger
import com.example.ui.theme.KeyActiveGlow

data class KeyDefinition(
    val primaryLabel: String,
    val shiftLabel: String? = null,
    val weight: Float = 1.0f,
    val isTactileMarker: Boolean = false,
    val outputChar: Char? = null,
    val shiftChar: Char? = null
)

@Composable
fun OnScreenKeyboard(
    targetChar: Char?,
    isShiftPressed: Boolean = false,
    showFingerColors: Boolean = true,
    showHints: Boolean = true,
    allowBackspace: Boolean = true,
    onKeyTapped: (Char) -> Unit,
    onBackspaceTapped: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val targetKeyUpper = targetChar?.uppercaseChar()?.toString() ?: ""
    val isSpaceTarget = targetChar == ' '

    // Pulsing animation for the target key
    val infiniteTransition = rememberInfiniteTransition(label = "keyGlow")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("on_screen_keyboard"),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Row 1: Numbers & symbols
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                renderKey("`", "~", 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, '`', '~')
                renderKey("1", "!", 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, '1', '!')
                renderKey("2", "@", 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, '2', '@')
                renderKey("3", "#", 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, '3', '#')
                renderKey("4", "$", 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, '4', '$')
                renderKey("5", "%", 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, '5', '%')
                renderKey("6", "^", 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, '6', '^')
                renderKey("7", "&", 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, '7', '&')
                renderKey("8", "*", 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, '8', '*')
                renderKey("9", "(", 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, '9', '(')
                renderKey("0", ")", 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, '0', ')')
                renderKey("-", "_", 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, '-', '_')
                renderKey("=", "+", 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, '=', '+')
                // Backspace (with visual disabled indicator if in exam mode)
                SpecialKey(
                    label = if (allowBackspace) "⌫" else "⌫ ✕",
                    weight = 1.4f,
                    onClick = onBackspaceTapped,
                    isBlocked = !allowBackspace,
                    testTag = "key_backspace"
                )
            }

            // Row 2: Q W E R T Y U I O P [ ] \
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                SpecialKey(label = "Tab", weight = 1.3f, onClick = { onKeyTapped('\t') }, testTag = "key_tab")
                renderKey("Q", null, 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, 'q', 'Q')
                renderKey("W", null, 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, 'w', 'W')
                renderKey("E", null, 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, 'e', 'E')
                renderKey("R", null, 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, 'r', 'R')
                renderKey("T", null, 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, 't', 'T')
                renderKey("Y", null, 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, 'y', 'Y')
                renderKey("U", null, 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, 'u', 'U')
                renderKey("I", null, 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, 'i', 'I')
                renderKey("O", null, 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, 'o', 'O')
                renderKey("P", null, 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, 'p', 'P')
                renderKey("[", "{", 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, '[', '{')
                renderKey("]", "}", 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, ']', '}')
                renderKey("\\", "|", 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, '\\', '|')
            }

            // Row 3: A S D F G H J K L ; ' Enter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                SpecialKey(label = "Caps", weight = 1.4f, onClick = {}, testTag = "key_caps")
                renderKey("A", null, 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, 'a', 'A')
                renderKey("S", null, 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, 's', 'S')
                renderKey("D", null, 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, 'd', 'D')
                renderKey("F", null, 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, 'f', 'F', isTactile = true)
                renderKey("G", null, 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, 'g', 'G')
                renderKey("H", null, 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, 'h', 'H')
                renderKey("J", null, 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, 'j', 'J', isTactile = true)
                renderKey("K", null, 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, 'k', 'K')
                renderKey("L", null, 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, 'l', 'L')
                renderKey(";", ":", 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, ';', ':')
                renderKey("'", "\"", 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, '\'', '"')
                SpecialKey(label = "Enter ⏎", weight = 1.6f, onClick = { onKeyTapped('\n') }, testTag = "key_enter")
            }

            // Row 4: Shift Z X C V B N M , . / Shift
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                val needShift = targetChar?.isUpperCase() == true
                SpecialKey(
                    label = "Shift ⇧",
                    weight = 1.6f,
                    isHighlighted = needShift && showHints,
                    onClick = {},
                    testTag = "key_shift_left"
                )
                renderKey("Z", null, 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, 'z', 'Z')
                renderKey("X", null, 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, 'x', 'X')
                renderKey("C", null, 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, 'c', 'C')
                renderKey("V", null, 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, 'v', 'V')
                renderKey("B", null, 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, 'b', 'B')
                renderKey("N", null, 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, 'n', 'N')
                renderKey("M", null, 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, 'm', 'M')
                renderKey(",", "<", 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, ',', '<')
                renderKey(".", ">", 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, '.', '>')
                renderKey("/", "?", 1f, targetKeyUpper, isShiftPressed, showFingerColors, showHints, pulseScale, onKeyTapped, '/', '?')
                SpecialKey(
                    label = "Shift ⇧",
                    weight = 1.6f,
                    isHighlighted = needShift && showHints,
                    onClick = {},
                    testTag = "key_shift_right"
                )
            }

            // Row 5: Spacebar & Modifiers
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                SpecialKey(label = "Ctrl", weight = 1.2f, onClick = {}, testTag = "key_ctrl_l")
                SpecialKey(label = "Alt", weight = 1.2f, onClick = {}, testTag = "key_alt_l")

                // Spacebar
                val spaceFinger = Finger.THUMBS
                val isSpaceHighlighted = isSpaceTarget && showHints
                val spaceBorderColor = if (isSpaceHighlighted) KeyActiveGlow else Color.Transparent
                val spaceBackground by animateColorAsState(
                    targetValue = if (isSpaceHighlighted) {
                        spaceFinger.color.copy(alpha = 0.35f)
                    } else {
                        MaterialTheme.colorScheme.surface
                    },
                    label = "spaceBg"
                )

                Box(
                    modifier = Modifier
                        .weight(5.5f)
                        .height(38.dp)
                        .scale(if (isSpaceHighlighted) pulseScale else 1.0f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(spaceBackground)
                        .border(
                            width = if (isSpaceHighlighted) 2.dp else 1.dp,
                            color = if (isSpaceHighlighted) spaceBorderColor else MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .clickable { onKeyTapped(' ') }
                        .testTag("key_space"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (isSpaceHighlighted) "␣ SPACE" else "SPACE",
                            fontSize = 11.sp,
                            fontWeight = if (isSpaceHighlighted) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSpaceHighlighted) spaceFinger.color else MaterialTheme.colorScheme.onSurface
                        )
                        if (showFingerColors) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 1.dp)
                                    .width(36.dp)
                                    .height(3.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(spaceFinger.color)
                            )
                        }
                    }
                }

                SpecialKey(label = "Alt", weight = 1.2f, onClick = {}, testTag = "key_alt_r")
                SpecialKey(label = "Ctrl", weight = 1.2f, onClick = {}, testTag = "key_ctrl_r")
            }
        }
    }
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.renderKey(
    primary: String,
    shift: String?,
    weight: Float,
    targetKeyUpper: String,
    isShiftPressed: Boolean,
    showFingerColors: Boolean,
    showHints: Boolean,
    pulseScale: Float,
    onKeyTapped: (Char) -> Unit,
    primaryChar: Char,
    shiftChar: Char,
    isTactile: Boolean = false
) {
    val isCurrentTarget = (primary.equals(targetKeyUpper, ignoreCase = true) ||
            (shift != null && shift == targetKeyUpper))
    val isHighlighted = isCurrentTarget && showHints
    val finger = Finger.getFingerForKey(primary)

    val keyBackground by animateColorAsState(
        targetValue = when {
            isHighlighted -> finger.color.copy(alpha = 0.35f)
            else -> MaterialTheme.colorScheme.surface
        },
        label = "keyBg"
    )

    val borderColor = if (isHighlighted) KeyActiveGlow else MaterialTheme.colorScheme.outlineVariant

    Box(
        modifier = Modifier
            .weight(weight)
            .height(38.dp)
            .scale(if (isHighlighted) pulseScale else 1.0f)
            .clip(RoundedCornerShape(6.dp))
            .background(keyBackground)
            .border(
                width = if (isHighlighted) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable {
                onKeyTapped(if (isShiftPressed) shiftChar else primaryChar)
            }
            .testTag("key_$primary"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (shift != null) {
                Text(
                    text = shift,
                    fontSize = 8.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    lineHeight = 9.sp
                )
            }
            Text(
                text = primary,
                fontSize = if (shift != null) 11.sp else 13.sp,
                fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Medium,
                color = if (isHighlighted) finger.color else MaterialTheme.colorScheme.onSurface
            )

            // Tactile bump indicator for F and J home row anchor keys
            if (isTactile) {
                Box(
                    modifier = Modifier
                        .padding(top = 1.dp)
                        .width(10.dp)
                        .height(2.dp)
                        .clip(RoundedCornerShape(1.dp))
                        .background(if (isHighlighted) KeyActiveGlow else MaterialTheme.colorScheme.primary)
                )
            } else if (showFingerColors) {
                // Subtle finger color indicator strip
                Box(
                    modifier = Modifier
                        .padding(top = 1.dp)
                        .width(12.dp)
                        .height(2.dp)
                        .clip(RoundedCornerShape(1.dp))
                        .background(finger.color)
                )
            }
        }
    }
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.SpecialKey(
    label: String,
    weight: Float,
    isHighlighted: Boolean = false,
    isBlocked: Boolean = false,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .weight(weight)
            .height(38.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(
                when {
                    isBlocked -> Color(0xFFFEE2E2)
                    isHighlighted -> MaterialTheme.colorScheme.primaryContainer
                    else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f)
                }
            )
            .border(
                width = if (isHighlighted || isBlocked) 2.dp else 1.dp,
                color = when {
                    isBlocked -> Color(0xFFDC2626)
                    isHighlighted -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.outlineVariant
                },
                shape = RoundedCornerShape(6.dp)
            )
            .clickable(onClick = onClick)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = when {
                isBlocked -> Color(0xFFDC2626)
                isHighlighted -> MaterialTheme.colorScheme.onPrimaryContainer
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}
