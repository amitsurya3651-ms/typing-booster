package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.database.entities.UserProfileEntity
import com.example.model.LessonCatalog
import com.example.viewmodel.Screen
import com.example.viewmodel.TypingViewModel

@Composable
fun HomeScreen(
    viewModel: TypingViewModel,
    userProfile: UserProfileEntity?,
    onOpenTestDialog: () -> Unit
) {
    val isHindi = userProfile?.language == "hi"
    val studentName = userProfile?.name ?: "Student"
    val nextLessonIndex = (userProfile?.lessonsCompleted ?: 0) + 1
    val nextLesson = LessonCatalog.getLessonById(nextLessonIndex.coerceIn(1, 12))

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top App Bar with Streak & Language Switcher
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Keyboard,
                        contentDescription = "Logo",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column {
                    Text(
                        text = "TYPING MASTER",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onBackground,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = if (isHindi) "टच टाइपिंग ट्यूटर" else "Touch Typing Tutor",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Streak Badge
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFFFEDD5),
                    modifier = Modifier.testTag("badge_streak")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Whatshot,
                            contentDescription = "Streak",
                            tint = Color(0xFFEA580C),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${userProfile?.dailyStreak ?: 1}d",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC2410C)
                        )
                    }
                }

                // Bilingual Toggle Button [ English / हिंदी ]
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier
                        .clickable { viewModel.toggleLanguage() }
                        .testTag("btn_language_toggle")
                ) {
                    Text(
                        text = if (isHindi) "English" else "हिंदी",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                IconButton(
                    onClick = { viewModel.navigateTo(Screen.SETTINGS) },
                    modifier = Modifier.testTag("btn_settings_top")
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Welcome Hero Card with Student Stats
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("card_welcome_hero"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isHindi) "नमस्ते, $studentName!" else "Welcome, $studentName!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (isHindi) "लेवल ${userProfile?.currentLevel ?: 1} • टच टाइपिंग शिक्षार्थी" else "Level ${userProfile?.currentLevel ?: 1} • Touch Typist",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Level circle badge
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "L${userProfile?.currentLevel ?: 1}",
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stats row: Current Speed, Accuracy, Best Speed
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatHighlight(
                        label = if (isHindi) "वर्तमान गति" else "Current Speed",
                        value = "${userProfile?.bestWpm ?: 0} WPM",
                        icon = Icons.Default.Speed,
                        accentColor = MaterialTheme.colorScheme.primary
                    )
                    StatHighlight(
                        label = if (isHindi) "सटीकता" else "Accuracy",
                        value = "${(userProfile?.averageAccuracy ?: 95f).toInt()}%",
                        icon = Icons.Default.CheckCircle,
                        accentColor = Color(0xFF10B981)
                    )
                    StatHighlight(
                        label = if (isHindi) "सर्वश्रेष्ठ गति" else "Best Speed",
                        value = "${userProfile?.bestWpm ?: 0} WPM",
                        icon = Icons.Default.Bolt,
                        accentColor = Color(0xFFF59E0B)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Secondary metrics: Lessons Completed & Total Practice Time
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (isHindi) "पाठ पूरे किए: ${userProfile?.lessonsCompleted ?: 0}/12" else "Lessons: ${userProfile?.lessonsCompleted ?: 0}/12",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    val minutesTotal = (userProfile?.totalPracticeTimeSeconds ?: 0L) / 60
                    Text(
                        text = if (isHindi) "अभ्यास समय: ${minutesTotal}m" else "Practice Time: ${minutesTotal}m",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Primary Action: [ CONTINUE LESSON ] (Large, prominent)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.startLesson(nextLesson) }
                .testTag("btn_continue_lesson"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Column {
                        Text(
                            text = if (isHindi) "पाठ जारी रखें (CONTINUE LESSON)" else "CONTINUE LESSON",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = if (isHindi) nextLesson.titleHi else nextLesson.titleEn,
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Text(
                            text = if (isHindi) nextLesson.subtitleHi else nextLesson.subtitleEn,
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.75f)
                        )
                    }
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Forward",
                    tint = Color.White
                )
            }
        }

        // Govt Exams & Long Paragraphs Special Action Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.navigateTo(Screen.EXAM_PARAGRAPHS) }
                .testTag("btn_home_exam_paragraphs"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E1B4B)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF38BDF8).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = if (isHindi) "सरकारी परीक्षा एवं लंबे अनुच्छेद" else "Govt Exams & Long Paragraphs",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFDC2626)
                            ) {
                                Text(
                                    text = "BSF HCM",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(
                            text = if (isHindi)
                                "BSF HCM (No Backspace), SSC CGL/CHSL, कोर्ट क्लर्क + सर्च बार"
                            else
                                "BSF HCM (No Backspace), SSC CGL/CHSL DEST, Court Typist + Search Bar",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color(0xFF38BDF8)
                )
            }
        }

        // Quick Action Grid (All Lessons, Typing Test, Weak Keys, My Progress)
        Text(
            text = if (isHindi) "मुख्य अभ्यास सुविधाएं" else "PRACTICE & TOOLS",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 0.5.sp
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Typing Test Button
            ActionGridCard(
                title = if (isHindi) "टाइपिंग परीक्षा" else "Typing Test",
                subtitle = if (isHindi) "1, 2, 5, 10 मिनट" else "1, 2, 5, 10 Min",
                icon = Icons.Default.Timer,
                iconTint = Color(0xFF0284C7),
                bgTint = Color(0xFFE0F2FE),
                modifier = Modifier.weight(1f),
                onClick = onOpenTestDialog,
                testTag = "btn_home_typing_test"
            )

            // All Lessons Button
            ActionGridCard(
                title = if (isHindi) "सभी 12 पाठ" else "12 Lessons",
                subtitle = if (isHindi) "शून्य से विशेषज्ञ" else "Step 1 to 12",
                icon = Icons.Default.Book,
                iconTint = Color(0xFF059669),
                bgTint = Color(0xFFD1FAE5),
                modifier = Modifier.weight(1f),
                onClick = { viewModel.navigateTo(Screen.LESSONS) },
                testTag = "btn_home_lessons"
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Weak Keys Button
            ActionGridCard(
                title = if (isHindi) "कमजोर कुंजियाँ" else "Weak Keys",
                subtitle = if (isHindi) "त्रुटि विश्लेषण" else "Error Analysis",
                icon = Icons.Default.Keyboard,
                iconTint = Color(0xFFDC2626),
                bgTint = Color(0xFFFEE2E2),
                modifier = Modifier.weight(1f),
                onClick = { viewModel.navigateTo(Screen.WEAK_KEYS) },
                testTag = "btn_home_weak_keys"
            )

            // Progress & Achievements Button
            ActionGridCard(
                title = if (isHindi) "मेरी प्रगति" else "My Progress",
                subtitle = if (isHindi) "ग्राफ और पदक" else "Stats & Badges",
                icon = Icons.Default.BarChart,
                iconTint = Color(0xFF7C3AED),
                bgTint = Color(0xFFEDE9FE),
                modifier = Modifier.weight(1f),
                onClick = { viewModel.navigateTo(Screen.PROGRESS) },
                testTag = "btn_home_progress"
            )
        }

        // Finger Placement Quick Tip Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("card_finger_tip"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "FJ",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Column {
                    Text(
                        text = if (isHindi) "टच टाइपिंग का मूल नियम (Golden Rule):" else "Touch Typing Golden Rule:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isHindi)
                            "कीबोर्ड पर देखे बिना टाइप करें। अपनी तर्जनी उंगलियों को F और J के उभारों (Bumps) पर महसूस करें।"
                        else
                            "Never look at your hands. Feel the raised bumps on F and J with your index fingers.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun StatHighlight(
    label: String,
    value: String,
    icon: ImageVector,
    accentColor: Color
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ActionGridCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    bgTint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick)
            .testTag(testTag),
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
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(bgTint),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
