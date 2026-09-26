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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.database.entities.KeyStatEntity
import com.example.model.Finger
import com.example.viewmodel.Screen
import com.example.viewmodel.TypingViewModel

@Composable
fun WeakKeysScreen(
    viewModel: TypingViewModel,
    keyStats: List<KeyStatEntity>,
    isHindi: Boolean
) {
    BackHandler {
        viewModel.navigateTo(Screen.HOME)
    }

    // Filter keys that have mistakes or lower accuracy
    val sortedWeakKeys = keyStats
        .filter { it.errorCount > 0 }
        .sortedBy { it.accuracyPercent }

    // Top weak keys for combined drill
    val topWeakCharList = sortedWeakKeys.take(4).mapNotNull { it.keyChar.firstOrNull() }

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
                modifier = Modifier.testTag("btn_back_weak_keys")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isHindi) "कमजोर कुंजियों का विश्लेषण" else "MY WEAK KEYS",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (isHindi) "गलत होने वाले अक्षरों का लक्षित अभ्यास" else "Personalized drills targeting lowest accuracy keys",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Personalized Practice Action Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("card_personalized_practice"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = if (isHindi) "व्यक्तिगत अभ्यास सत्र" else "Personalized Practice Session",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (topWeakCharList.isNotEmpty()) {
                            Text(
                                text = if (isHindi)
                                    "आपके सबसे कमजोर अक्षर: ${topWeakCharList.joinToString(", ")}"
                                else
                                    "Your weak keys are: ${topWeakCharList.joinToString(", ")}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = if (isHindi)
                                    "यह सत्र विशेष रूप से आपके द्वारा की गई गलतियों के आधार पर शब्द अभ्यास तैयार करेगा।"
                                else
                                    "This drill automatically generates custom words heavily featuring your mistyped keys.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                        } else {
                            Text(
                                text = if (isHindi)
                                    "अभी तक कोई गंभीर गलतियाँ दर्ज नहीं हैं! होम रो के प्रमुख अक्षरों का अभ्यास करें।"
                                else
                                    "No weak keys detected yet! Practice key combinations to build initial muscle memory.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                val keys = if (topWeakCharList.isNotEmpty()) topWeakCharList else listOf('d', 'k', 'r', 'f')
                                viewModel.startWeakKeyDrill(keys)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_start_personalized_practice"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.padding(end = 6.dp))
                            Text(
                                text = if (isHindi) "व्यक्तिगत अभ्यास शुरू करें [ START PRACTICE ]" else "START PERSONALIZED PRACTICE",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Section Header
            item {
                Text(
                    text = if (isHindi) "कुंजी सटीकता सूची (Key Accuracy)" else "ACCURACY BREAKDOWN",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 0.5.sp
                )
            }

            // Weak Keys List
            if (sortedWeakKeys.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (isHindi) "सभी कुंजियाँ अच्छी स्थिति में हैं!" else "All keys performing well!",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (isHindi) "और अभ्यास या टेस्ट पूरा करने पर कमजोर कुंजियाँ यहाँ दिखेंगी।" else "Complete typing tests to track error patterns and key accuracy.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(sortedWeakKeys) { stat ->
                    val char = stat.keyChar.firstOrNull() ?: ' '
                    val finger = Finger.getFingerForChar(char)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("weak_key_card_${stat.keyChar}"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Key Cap Box
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(finger.color.copy(alpha = 0.2f))
                                    .border(2.dp, finger.color, RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stat.keyChar,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = finger.color
                                )
                            }

                            // Accuracy & Error count Column
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "${stat.accuracyPercent}% ${if (isHindi) "सटीकता" else "accuracy"}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (stat.accuracyPercent < 80) Color(0xFFEF4444) else Color(0xFFF59E0B)
                                    )
                                    Text(
                                        text = "${stat.errorCount} ${if (isHindi) "त्रुटियाँ" else "errors"}",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                LinearProgressIndicator(
                                    progress = { (stat.accuracyPercent / 100f).coerceIn(0f, 1f) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = if (stat.accuracyPercent < 80) Color(0xFFEF4444) else Color(0xFF10B981),
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = if (isHindi) finger.nameHi else finger.nameEn,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            // Single key practice button [ PRACTICE X ]
                            OutlinedButton(
                                onClick = { viewModel.startSingleKeyDrill(char) },
                                modifier = Modifier.testTag("btn_practice_${stat.keyChar}"),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "PRACTICE ${stat.keyChar}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
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
