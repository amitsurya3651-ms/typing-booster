package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.database.entities.UserProfileEntity
import com.example.viewmodel.Screen
import com.example.viewmodel.TypingViewModel

@Composable
fun SettingsScreen(
    viewModel: TypingViewModel,
    userProfile: UserProfileEntity?,
    isHindi: Boolean
) {
    BackHandler {
        viewModel.navigateTo(Screen.HOME)
    }

    var showNameDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var editedName by remember(userProfile?.name) { mutableStateOf(userProfile?.name ?: "Student") }

    val scrollState = rememberScrollState()

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
                modifier = Modifier.testTag("btn_back_settings")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isHindi) "सेटिंग्स (Settings)" else "SETTINGS",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (isHindi) "भाषा, ध्वनि, उंगली रंग और प्राथमिकताएं" else "Language, audio, finger colors, and preferences",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Student Profile Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = if (isHindi) "विद्यार्थी का नाम" else "Student Name",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = userProfile?.name ?: "Student",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(
                        onClick = { showNameDialog = true },
                        modifier = Modifier.testTag("btn_edit_name")
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Name", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            // Tutor Preferences Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Language Switch
                    SettingToggleRow(
                        title = if (isHindi) "भाषा (Language)" else "Language",
                        subtitle = if (isHindi) "वर्तमान: हिंदी (English में बदलें)" else "Current: English (Switch to हिंदी)",
                        icon = Icons.Default.Language,
                        checked = isHindi,
                        onCheckedChange = { viewModel.toggleLanguage() },
                        testTag = "toggle_language"
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    // Keypress Sound
                    SettingToggleRow(
                        title = if (isHindi) "कुंजी ध्वनि (Sound)" else "Keypress Sound Effects",
                        subtitle = if (isHindi) "टाइप करने और गलती पर आवाज़" else "Audio click on keypress and error buzzer",
                        icon = Icons.Default.VolumeUp,
                        checked = userProfile?.soundEnabled ?: true,
                        onCheckedChange = { viewModel.updateSettings(soundEnabled = it) },
                        testTag = "toggle_sound"
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    // Vibration
                    SettingToggleRow(
                        title = if (isHindi) "कंपन (Vibration Feedback)" else "Haptic Vibration",
                        subtitle = if (isHindi) "गलती होने पर हल्का कंपन" else "Vibrate device gently upon error",
                        icon = Icons.Default.Vibration,
                        checked = userProfile?.hapticsEnabled ?: true,
                        onCheckedChange = { viewModel.updateSettings(hapticsEnabled = it) },
                        testTag = "toggle_haptics"
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    // Finger Colors
                    SettingToggleRow(
                        title = if (isHindi) "उंगली रंग निर्देश (Finger Colors)" else "Finger Color Guidance",
                        subtitle = if (isHindi) "कीबोर्ड कुंजियों पर उंगलियों के रंग" else "Color-coded keys for 8 fingers and thumbs",
                        icon = Icons.Default.Palette,
                        checked = userProfile?.fingerColorsEnabled ?: true,
                        onCheckedChange = { viewModel.updateSettings(fingerColorsEnabled = it) },
                        testTag = "toggle_finger_colors"
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    // Keyboard Visual Hints
                    SettingToggleRow(
                        title = if (isHindi) "कीबोर्ड संकेत (Next Key Glow)" else "Visual Key Guidance",
                        subtitle = if (isHindi) "अगली दबने वाली कुंजी को चमकाना" else "Highlight next expected key and finger on screen",
                        icon = Icons.Default.TextFields,
                        checked = userProfile?.keyboardHintsEnabled ?: true,
                        onCheckedChange = { viewModel.updateSettings(keyboardHintsEnabled = it) },
                        testTag = "toggle_hints"
                    )
                }
            }

            // Danger Zone: Reset Data
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isHindi) "प्रगति रीसेट करें" else "Reset All Progress",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFDC2626)
                        )
                        Text(
                            text = if (isHindi) "सभी टेस्ट, स्पीड रिकॉर्ड और आंकड़े मिटाएं" else "Clear all test results, speed history, and key stats",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = { showResetDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("btn_reset_data")
                    ) {
                        Text(text = if (isHindi) "रीसेट" else "Reset")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Edit Name Dialog
        if (showNameDialog) {
            AlertDialog(
                onDismissRequest = { showNameDialog = false },
                title = { Text(text = if (isHindi) "विद्यार्थी का नाम बदलें" else "Edit Student Name") },
                text = {
                    OutlinedTextField(
                        value = editedName,
                        onValueChange = { editedName = it },
                        label = { Text(if (isHindi) "नाम दर्ज करें" else "Your Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.updateSettings(name = editedName.trim().ifEmpty { "Student" })
                            showNameDialog = false
                        }
                    ) {
                        Text(text = if (isHindi) "सेव करें" else "Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showNameDialog = false }) {
                        Text(text = if (isHindi) "रद्द करें" else "Cancel")
                    }
                }
            )
        }

        // Reset Confirmation Dialog
        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = { showResetDialog = false },
                title = { Text(text = if (isHindi) "क्या आप वाकई रीसेट करना चाहते हैं?" else "Reset All Data?") },
                text = {
                    Text(
                        text = if (isHindi)
                            "यह आपके सभी टेस्ट परिणाम, कमजोर कुंजियों का डेटा, स्पीड इतिहास और स्ट्रीक को हमेशा के लिए मिटा देगा।"
                        else
                            "This action will permanently delete all your test records, weak key statistics, speed scores, and streak."
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.resetAllData()
                            showResetDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                    ) {
                        Text(text = if (isHindi) "हाँ, सब मिटाएं" else "Yes, Delete All")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showResetDialog = false }) {
                        Text(text = if (isHindi) "रद्द करें" else "Cancel")
                    }
                }
            )
        }
    }
}

@Composable
private fun SettingToggleRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )

            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.testTag(testTag)
        )
    }
}
