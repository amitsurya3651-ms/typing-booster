package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TestSelectionDialog(
    isHindi: Boolean,
    onDismiss: () -> Unit,
    onBrowseExamPassages: () -> Unit = {},
    onStartTest: (durationSeconds: Int, title: String, passage: String, allowBackspace: Boolean) -> Unit
) {
    var selectedDuration by remember { mutableIntStateOf(600) } // Default 10 min official test
    var allowBackspace by remember { mutableStateOf(true) }

    val passage1Min = "Practice typing with a relaxed posture and smooth finger motions. Accuracy is always more important than speed in the beginning. As your muscle memory solidifies, your fingers will instinctively fly across the keyboard."
    val passage2Min = "Digital communication requires fast and precise keyboard skills. Touch typing allows you to focus on the content of your thoughts rather than searching for letters. Keep your palms gently elevated, shoulders relaxed, and maintain a consistent rhythm with your breathing."
    val passage5Min = "Computers and mobile keyboards have transformed education and employment worldwide. For competitive government examinations and clerical certifications, typing speed standards often demand between thirty and forty words per minute with ninety-five percent accuracy. Achieving this goal is a matter of daily deliberate practice. Do not try to type faster than your fingers can accurately manage. When you hit a stumbling block with a tricky word, slow down momentarily, press the exact sequence of keys cleanly, and return to your natural cadence."
    val passage10Min = "In the modern digital era, typing speed is an essential qualification for competitive examinations and professional success. Whether you are preparing for government typing skill tests, writing reports, coding applications, or communicating in business, mastering touch typing saves hundreds of productive hours every year. Touch typing relies entirely on tactile muscle memory rather than sight. The ten fingers rest gently on the home row keys, with the left index finger on F and the right index finger on J. These two anchor keys feature raised bumps that guide your hands back to their base positions without ever glancing down at the physical keyboard. When you encounter a challenging key, pause for a microsecond rather than striking the wrong letter. Accuracy is the mother of speed. Students who concentrate on typing correctly develop flawless subconscious reflexes. Over weeks of disciplined training, complex letter patterns and vocabulary flow directly from thought onto the screen with zero friction. Keep your posture upright, breathe calmly, and let your fingers find their natural rhythm. Consistent daily effort is the secret that separates ordinary typists from true typing masters."

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = if (isHindi) "टाइपिंग परीक्षा चुनें" else "Choose Typing Test",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = if (isHindi) "परीक्षा अवधि चुनें:" else "Select Duration:",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                val durations = listOf(
                    Triple(60, "1 Minute Test", "1 मिनट टेस्ट"),
                    Triple(120, "2 Minutes Test", "2 मिनट टेस्ट"),
                    Triple(300, "5 Minutes Test", "5 मिनट टेस्ट"),
                    Triple(600, "10 Minutes Exam Test", "10 मिनट परीक्षा टेस्ट (मानक)")
                )

                durations.forEach { (seconds, en, hi) ->
                    val isSelected = selectedDuration == seconds
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedDuration = seconds }
                            .testTag("test_option_$seconds"),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ),
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 9.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = if (isHindi) hi else en,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                )
                                if (seconds == 600) {
                                    Text(
                                        text = if (isHindi) "सरकारी व प्रतियोगी परीक्षा मानक" else "Recommended for competitive exam practice",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            if (isSelected) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.primary
                                ) {
                                    Text(
                                        text = "✓",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Backspace Rule Toggle
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { allowBackspace = !allowBackspace },
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (!allowBackspace) Color(0xFFFEF2F2) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ),
                    border = if (!allowBackspace) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5)) else null
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (!allowBackspace) {
                                    if (isHindi) "नो बैकस्पेस मोड (BSF HCM नियम)" else "BSF HCM Mode (No Backspace)"
                                } else {
                                    if (isHindi) "सामान्य मोड (बैकस्पेस चालू)" else "Normal Mode (Backspace Allowed)"
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (!allowBackspace) Color(0xFFDC2626) else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (!allowBackspace) {
                                    if (isHindi) "गलती सुधारने की अनुमति नहीं होगी" else "Zero corrections allowed (Force exam rules)"
                                } else {
                                    if (isHindi) "SSC CGL/CHSL की तरह बैकस्पेस चालू" else "Backspace permitted for corrections"
                                },
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (!allowBackspace) Color(0xFFDC2626) else Color(0xFF10B981)
                        ) {
                            Text(
                                text = if (!allowBackspace) "NO BKSP" else "BKSP OK",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                // Link to full exam paragraphs catalog
                TextButton(
                    onClick = onBrowseExamPassages,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isHindi) "सरकारी परीक्षा के लंबे अनुच्छेद देखें →" else "Browse Govt Exam Passages & Rules →",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val (passage, title) = when (selectedDuration) {
                        60 -> passage1Min to "1-Minute Typing Test"
                        120 -> passage2Min to "2-Minute Typing Test"
                        300 -> passage5Min to "5-Minute Typing Test"
                        else -> passage10Min to if (!allowBackspace) "10-Min BSF HCM Exam Test" else "10-Minute Exam Typing Test"
                    }
                    onStartTest(selectedDuration, title, passage, allowBackspace)
                },
                modifier = Modifier.testTag("btn_confirm_start_test")
            ) {
                Text(text = if (isHindi) "परीक्षा शुरू करें" else "Start Test")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = if (isHindi) "रद्द करें" else "Cancel")
            }
        }
    )
}
