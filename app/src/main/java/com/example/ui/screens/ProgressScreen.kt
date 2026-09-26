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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.database.entities.AchievementEntity
import com.example.database.entities.TestResultEntity
import com.example.database.entities.UserProfileEntity
import com.example.viewmodel.Screen
import com.example.viewmodel.TypingViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProgressScreen(
    viewModel: TypingViewModel,
    userProfile: UserProfileEntity?,
    testResults: List<TestResultEntity>,
    achievements: List<AchievementEntity>,
    isHindi: Boolean
) {
    BackHandler {
        viewModel.navigateTo(Screen.HOME)
    }

    val recentResults = testResults.take(10).reversed()
    val maxWpm = (recentResults.maxOfOrNull { it.netWpm } ?: 40).coerceAtLeast(30)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // App bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateTo(Screen.HOME) },
                modifier = Modifier.testTag("btn_back_progress")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isHindi) "प्रगति और उपलब्धियां" else "MY PROGRESS",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (isHindi) "स्पीड चार्ट, टेस्ट इतिहास और पदक" else "Speed growth charts, test history and milestone badges",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Lifetime Overview Cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = if (isHindi) "सर्वश्रेष्ठ स्पीड" else "Best Speed",
                        value = "${userProfile?.bestWpm ?: 0} WPM",
                        icon = Icons.Default.Bolt,
                        accentColor = Color(0xFFF59E0B),
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = if (isHindi) "औसत सटीकता" else "Avg Accuracy",
                        value = "${(userProfile?.averageAccuracy ?: 95f).toInt()}%",
                        icon = Icons.Default.CheckCircle,
                        accentColor = Color(0xFF10B981),
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = if (isHindi) "अभ्यास स्ट्रीक" else "Daily Streak",
                        value = "${userProfile?.dailyStreak ?: 1}d",
                        icon = Icons.Default.Whatshot,
                        accentColor = Color(0xFFEA580C),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Speed Growth Bar Chart
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("card_speed_chart"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = if (isHindi) "गति सुधार चार्ट (WPM Growth Trend)" else "Speed Progress Trend (Recent Tests)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        if (recentResults.isEmpty()) {
                            Text(
                                text = if (isHindi) "कोई टेस्ट डेटा नहीं मिला। चार्ट देखने के लिए अभ्यास करें।" else "No test data available yet. Complete a test to see your progress chart!",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                recentResults.forEachIndexed { index, test ->
                                    val barHeightRatio = (test.netWpm.toFloat() / maxWpm).coerceIn(0.1f, 1f)
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Bottom,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = "${test.netWpm}",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Box(
                                            modifier = Modifier
                                                .width(14.dp)
                                                .height((80 * barHeightRatio).dp)
                                                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                                .background(MaterialTheme.colorScheme.primary)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "T${index + 1}",
                                            fontSize = 9.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Achievements Section
            item {
                Text(
                    text = if (isHindi) "उपलब्धियां और पदक (Achievements)" else "ACHIEVEMENTS & BADGES",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 0.5.sp
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        achievements.forEach { achievement ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (achievement.isUnlocked) Color(0xFFFEF3C7) else MaterialTheme.colorScheme.surfaceVariant
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (achievement.isUnlocked) Icons.Default.EmojiEvents else Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = if (achievement.isUnlocked) Color(0xFFD97706) else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (isHindi) achievement.titleHi else achievement.titleEn,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (achievement.isUnlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = if (isHindi) achievement.descriptionHi else achievement.descriptionEn,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                if (achievement.isUnlocked) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFFD1FAE5)
                                    ) {
                                        Text(
                                            text = if (isHindi) "प्राप्त" else "UNLOCKED",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF065F46),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Test History Section
            item {
                Text(
                    text = if (isHindi) "हाल के टेस्ट रिकॉर्ड (Recent Test History)" else "RECENT TEST HISTORY",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 0.5.sp
                )
            }

            if (testResults.isEmpty()) {
                item {
                    Text(
                        text = if (isHindi) "अभी तक कोई टेस्ट रिकॉर्ड नहीं है।" else "No tests recorded yet.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(testResults.take(15)) { test ->
                    val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
                    val dateFormatted = sdf.format(Date(test.timestamp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("test_history_item_${test.id}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = test.title,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "$dateFormatted • ${test.durationSeconds}s",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "${test.netWpm} WPM",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "Gross: ${test.grossWpm}",
                                        fontSize = 9.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "${test.accuracy.toInt()}%",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (test.accuracy >= 90) Color(0xFF10B981) else Color(0xFFEF4444)
                                    )
                                    Text(
                                        text = "${test.incorrectCharacters} err",
                                        fontSize = 9.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface)
            Text(text = title, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
