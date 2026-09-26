package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.database.entities.TestResultEntity
import com.example.model.LessonCatalog
import com.example.viewmodel.Screen
import com.example.viewmodel.TypingViewModel

@Composable
fun ResultScreen(
    viewModel: TypingViewModel,
    latestResult: TestResultEntity?,
    previousResult: TestResultEntity?,
    isHindi: Boolean
) {
    BackHandler {
        viewModel.navigateTo(Screen.HOME)
    }

    val result = latestResult ?: return
    val scrollState = rememberScrollState()

    // Parse mistyped keys from json "D:12,K:10,R:8"
    val mistypedList = result.mistypedKeysJson
        .split(",")
        .filter { it.contains(":") }
        .mapNotNull {
            val parts = it.split(":")
            if (parts.size == 2) {
                val key = parts[0].trim()
                val count = parts[1].trim().toIntOrNull() ?: 0
                if (key.isNotEmpty() && count > 0) key to count else null
            } else null
        }
        .sortedByDescending { it.second }

    val weakCharList = mistypedList.map { it.first.first() }

    // Comparison calculation
    val speedDiff = if (previousResult != null) result.netWpm - previousResult.netWpm else null
    val accuracyDiff = if (previousResult != null) (result.accuracy - previousResult.accuracy).toInt() else null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Result Header Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("card_test_result_header"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Trophy",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (isHindi) "परीक्षा परिणाम (TEST RESULT)" else "TEST RESULT",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    letterSpacing = 1.sp
                )

                Text(
                    text = result.title,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Primary Speed Metrics (Net WPM & Gross WPM)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    MetricHighlight(
                        label = if (isHindi) "नेट स्पीड (Net WPM)" else "Net WPM",
                        value = "${result.netWpm}",
                        valueColor = MaterialTheme.colorScheme.primary
                    )
                    MetricHighlight(
                        label = if (isHindi) "ग्रॉस स्पीड (Gross WPM)" else "Gross WPM",
                        value = "${result.grossWpm}",
                        valueColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    MetricHighlight(
                        label = if (isHindi) "सटीकता (Accuracy)" else "Accuracy",
                        value = "${result.accuracy.toInt()}%",
                        valueColor = if (result.accuracy >= 90) Color(0xFF10B981) else Color(0xFFEF4444)
                    )
                }
            }
        }

        // Performance Comparison Card (vs Previous Test)
        if (speedDiff != null && accuracyDiff != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("card_comparison"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Text(
                        text = if (isHindi) "पिछले टेस्ट से तुलना (Comparison):" else "Comparison With Previous Test:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            text = "Speed: " + if (speedDiff >= 0) "+$speedDiff WPM ▲" else "$speedDiff WPM ▼",
                            fontWeight = FontWeight.Bold,
                            color = if (speedDiff >= 0) Color(0xFF10B981) else Color(0xFFEF4444),
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Accuracy: " + if (accuracyDiff >= 0) "+$accuracyDiff% ▲" else "$accuracyDiff% ▼",
                            fontWeight = FontWeight.Bold,
                            color = if (accuracyDiff >= 0) Color(0xFF10B981) else Color(0xFFEF4444),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // Character Breakdown Details Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("card_breakdown"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                Text(
                    text = if (isHindi) "विस्तृत विवरण (Details)" else "Keystroke Statistics",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(10.dp))

                DetailRow(
                    label = if (isHindi) "कुल अक्षर (Total Characters)" else "Total Characters Typed",
                    value = "${result.totalCharacters}"
                )
                DetailRow(
                    label = if (isHindi) "सही अक्षर (Correct Characters)" else "Correct Characters",
                    value = "${result.correctCharacters}",
                    valueColor = Color(0xFF10B981)
                )
                DetailRow(
                    label = if (isHindi) "गलत अक्षर (Incorrect Characters)" else "Incorrect Characters",
                    value = "${result.incorrectCharacters}",
                    valueColor = if (result.incorrectCharacters > 0) Color(0xFFEF4444) else Color(0xFF10B981)
                )
                val durationMin = result.durationSeconds / 60
                val durationSec = result.durationSeconds % 60
                DetailRow(
                    label = if (isHindi) "समय (Time Taken)" else "Time Taken",
                    value = String.format("%02d:%02d", durationMin, durationSec)
                )
                if (viewModel.engine.blockedBackspaceCount > 0) {
                    DetailRow(
                        label = if (isHindi) "अवरुद्ध बैकस्पेस प्रयास (BSF HCM)" else "Blocked Backspaces (BSF Rules)",
                        value = "${viewModel.engine.blockedBackspaceCount}",
                        valueColor = Color(0xFFDC2626)
                    )
                }
            }
        }

        // Error Analysis Card (Mistyped Keys)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("card_error_analysis"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Errors",
                        tint = if (mistypedList.isNotEmpty()) Color(0xFFEF4444) else Color(0xFF10B981),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = if (isHindi) "त्रुटि विश्लेषण (Error Analysis)" else "Mistyped Key Analysis",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (mistypedList.isEmpty()) {
                    Text(
                        text = if (isHindi) "उत्कृष्ट! कोई गलती नहीं हुई। शून्य त्रुटियाँ!" else "Flawless! Zero errors in this session.",
                        fontSize = 12.sp,
                        color = Color(0xFF10B981),
                        fontWeight = FontWeight.SemiBold
                    )
                } else {
                    Text(
                        text = if (isHindi) "सर्वाधिक गलत होने वाली कुंजियाँ:" else "Most Mistyped Keys:",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        mistypedList.take(5).forEach { (key, count) ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFFEE2E2),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = key,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 14.sp,
                                        color = Color(0xFFDC2626)
                                    )
                                    Text(
                                        text = "→ $count",
                                        fontSize = 11.sp,
                                        color = Color(0xFFB91C1C)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (isHindi)
                            "सुझाव: आपको इन अक्षरों का और अभ्यास करना चाहिए: ${weakCharList.take(5).joinToString(", ")}"
                        else
                            "Improvement: You need more practice with: ${weakCharList.take(5).joinToString(", ")}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Action Buttons
        if (weakCharList.isNotEmpty()) {
            Button(
                onClick = { viewModel.startWeakKeyDrill(weakCharList.take(4)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_practice_weak_keys_result"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFDC2626)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Keyboard,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 6.dp)
                )
                Text(
                    text = if (isHindi) "कमजोर कुंजियों का अभ्यास करें [ PRACTICE WEAK KEYS ]" else "PRACTICE WEAK KEYS",
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = { viewModel.restartSession() },
                modifier = Modifier
                    .weight(1f)
                    .testTag("btn_retry_test"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
                Text(text = if (isHindi) "पुनः प्रयास" else "Retry")
            }

            Button(
                onClick = {
                    val nextLessonId = (result.lessonId ?: 0) + 1
                    if (nextLessonId in 1..12) {
                        viewModel.startLesson(LessonCatalog.getLessonById(nextLessonId))
                    } else {
                        viewModel.navigateTo(Screen.HOME)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("btn_next_action"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = if (isHindi) "अगला पाठ" else "Next Lesson")
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.padding(start = 4.dp))
            }
        }

        OutlinedButton(
            onClick = { viewModel.navigateTo(Screen.HOME) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("btn_back_home"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(imageVector = Icons.Default.Home, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
            Text(text = if (isHindi) "डैशबोर्ड पर वापस जाएं" else "Back to Dashboard")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun MetricHighlight(label: String, value: String, valueColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            color = valueColor
        )
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun DetailRow(label: String, value: String, valueColor: Color = MaterialTheme.colorScheme.onSurface) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = valueColor)
    }
}
