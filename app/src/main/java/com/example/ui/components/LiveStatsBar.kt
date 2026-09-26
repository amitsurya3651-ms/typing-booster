package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.ui.theme.TypingCorrect
import com.example.ui.theme.TypingIncorrect

@Composable
fun LiveStatsBar(
    timeDisplay: String,
    grossWpm: Int,
    netWpm: Int,
    accuracy: Float,
    errors: Int,
    isRunning: Boolean,
    isHindi: Boolean,
    onTogglePause: () -> Unit,
    onRestart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("live_stats_bar"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Timer Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = "Timer",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 2.dp)
                )
                Column {
                    Text(
                        text = if (isHindi) "समय" else "TIME",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = timeDisplay,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Net WPM Badge
            StatPill(
                title = if (isHindi) "नेट WPM" else "NET WPM",
                value = "$netWpm",
                valueColor = MaterialTheme.colorScheme.onSurface,
                subtitle = "Gross: $grossWpm"
            )

            // Accuracy % Badge
            StatPill(
                title = if (isHindi) "सटीकता" else "ACCURACY",
                value = "${accuracy.toInt()}%",
                valueColor = if (accuracy >= 90) TypingCorrect else TypingIncorrect
            )

            // Errors Badge
            StatPill(
                title = if (isHindi) "त्रुटियाँ" else "ERRORS",
                value = "$errors",
                valueColor = if (errors == 0) TypingCorrect else TypingIncorrect
            )

            // Controls (Pause / Resume, Restart)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                IconButton(
                    onClick = onTogglePause,
                    modifier = Modifier.testTag("btn_pause_resume")
                ) {
                    Icon(
                        imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isRunning) "Pause" else "Resume",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(
                    onClick = onRestart,
                    modifier = Modifier.testTag("btn_restart")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Restart",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun StatPill(
    title: String,
    value: String,
    valueColor: Color,
    subtitle: String? = null
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold,
            color = valueColor
        )
        if (subtitle != null) {
            Text(
                text = subtitle,
                fontSize = 8.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
