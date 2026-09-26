package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ExamParagraph
import com.example.model.ExamParagraphCatalog
import com.example.model.ExamRuleGuide
import com.example.viewmodel.Screen
import com.example.viewmodel.TypingViewModel

@Composable
fun ExamParagraphsScreen(
    viewModel: TypingViewModel,
    isHindi: Boolean
) {
    BackHandler {
        viewModel.navigateTo(Screen.HOME)
    }

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("ALL") }
    var showRulesDialog by remember { mutableStateOf(false) }

    val filteredParagraphs = remember(searchQuery, selectedFilter) {
        ExamParagraphCatalog.search(searchQuery, selectedFilter)
    }

    val filterOptions = listOf(
        "ALL" to if (isHindi) "सभी अनुच्छेद" else "All Passages",
        "BSF" to if (isHindi) "BSF HCM (No Backspace)" else "BSF HCM (No Backspace)",
        "SSC" to if (isHindi) "SSC CGL / CHSL" else "SSC CGL / CHSL",
        "COURT" to if (isHindi) "हाई कोर्ट / क्लर्क" else "Court / Legal",
        "NO_BACKSPACE" to if (isHindi) "बिना बैकस्पेस वाले" else "No Backspace Only"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // App Bar with Rules Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateTo(Screen.HOME) },
                modifier = Modifier.testTag("btn_back_exam_paragraphs")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isHindi) "सरकारी परीक्षा एवं लंबे अनुच्छेद" else "Govt Exams & Long Paragraphs",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (isHindi) "BSF HCM, SSC CGL/CHSL, कोर्ट परीक्षा मॉक टेस्ट" else "BSF HCM, SSC CGL/CHSL & Court Typing Practice",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Rules button
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier
                    .clickable { showRulesDialog = true }
                    .testTag("btn_exam_rules")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Gavel,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (isHindi) "नियम" else "Exam Rules",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        // Search Bar for finding paragraphs
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_paragraphs_input"),
                placeholder = {
                    Text(
                        text = if (isHindi) "खोजें: BSF HCM, SSC, Economy, Court..." else "Search (e.g. 'BSF HCM', 'SSC CGL', 'Court')...",
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                shape = RoundedCornerShape(14.dp),
                singleLine = true
            )
        }

        // Horizontal Category Filter Chips
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filterOptions) { (key, label) ->
                val isSelected = selectedFilter == key
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedFilter = key },
                    label = {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    modifier = Modifier.testTag("filter_chip_$key")
                )
            }
        }

        // Paragraph Count Summary
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (isHindi) "उपलब्ध अनुच्छेद: ${filteredParagraphs.size}" else "Available Passages: ${filteredParagraphs.size}",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (selectedFilter == "BSF" || selectedFilter == "NO_BACKSPACE") {
                Text(
                    text = if (isHindi) "⚠️ बैकस्पेस पूर्णतः बंद रहेगा" else "⚠️ Backspace Disabled",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFDC2626)
                )
            }
        }

        // Paragraph List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (filteredParagraphs.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
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
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (isHindi) "कोई अनुच्छेद नहीं मिला" else "No matching paragraphs found",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = if (isHindi) "कृपया अन्य खोज शब्द या फ़िल्टर आज़माएं।" else "Try adjusting your search terms or filter selection.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(filteredParagraphs) { paragraph ->
                    ExamParagraphCard(
                        paragraph = paragraph,
                        isHindi = isHindi,
                        onStart = { viewModel.startExamParagraph(paragraph) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Exam Rules & Guidelines Dialog
    if (showRulesDialog) {
        AlertDialog(
            onDismissRequest = { showRulesDialog = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Gavel, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text(
                        text = if (isHindi) "सरकारी एवं बैंक परीक्षा टाइपिंग नियम" else "Govt & Bank Exam Typing Rules",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            },
            text = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(ExamRuleGuide.officialRules) { rule ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (!rule.backspaceAllowed) Color(0xFFFEF2F2) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ),
                            border = if (!rule.backspaceAllowed) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5)) else null
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (isHindi) rule.hindiName else rule.examName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (!rule.backspaceAllowed) Color(0xFFB91C1C) else MaterialTheme.colorScheme.onSurface
                                    )

                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (!rule.backspaceAllowed) Color(0xFFDC2626) else Color(0xFF10B981)
                                    ) {
                                        Text(
                                            text = if (!rule.backspaceAllowed) "NO BACKSPACE" else "BACKSPACE OK",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${if (isHindi) "आवश्यक गति: " else "Speed: "}${rule.speedRequirement} • ${rule.durationText}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (isHindi) rule.backspaceRuleHi else rule.backspaceRuleEn,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (!rule.backspaceAllowed) Color(0xFF991B1B) else MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${if (isHindi) "गलतियों का नियम: " else "Penalty: "}${if (isHindi) rule.errorPenaltyHi else rule.errorPenaltyEn}",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showRulesDialog = false }) {
                    Text(text = if (isHindi) "समझ गया (Got it)" else "Got it")
                }
            }
        )
    }
}

@Composable
private fun ExamParagraphCard(
    paragraph: ExamParagraph,
    isHindi: Boolean,
    onStart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onStart)
            .testTag("exam_paragraph_${paragraph.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = if (!paragraph.allowBackspace) {
            androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFCA5A5))
        } else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Badges row: Exam Tag, Duration, Backspace badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = paragraph.examTag,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Timer, contentDescription = null, modifier = Modifier.size(11.dp))
                            Text(
                                text = "${paragraph.durationMinutes}m",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = "• ${paragraph.wordCount} words",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Backspace Status Badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (!paragraph.allowBackspace) Color(0xFFFEE2E2) else Color(0xFFD1FAE5)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = if (!paragraph.allowBackspace) Icons.Default.Block else Icons.Default.Check,
                            contentDescription = null,
                            tint = if (!paragraph.allowBackspace) Color(0xFFDC2626) else Color(0xFF059669),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = if (!paragraph.allowBackspace) {
                                if (isHindi) "नो बैकस्पेस" else "No Backspace"
                            } else {
                                if (isHindi) "बैकस्पेस चालू" else "Backspace OK"
                            },
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (!paragraph.allowBackspace) Color(0xFFDC2626) else Color(0xFF059669)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isHindi) paragraph.titleHi else paragraph.titleEn,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Paragraph excerpt
            Text(
                text = paragraph.passageText.take(130) + "...",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Key instruction & Start button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isHindi) paragraph.keyPointsHi else paragraph.keyPointsEn,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (!paragraph.allowBackspace) Color(0xFFDC2626) else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = if (isHindi) "टाइप करें" else "Start Test",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}
