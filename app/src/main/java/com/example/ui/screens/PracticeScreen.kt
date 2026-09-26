package com.example.ui.screens

import android.view.KeyEvent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.key.utf16CodePoint
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.FingerPlacementGuide
import com.example.ui.components.LiveStatsBar
import com.example.ui.components.OnScreenKeyboard
import com.example.ui.components.PracticeTextView
import com.example.viewmodel.Screen
import com.example.viewmodel.TypingViewModel

@Composable
fun PracticeScreen(
    viewModel: TypingViewModel,
    isHindi: Boolean,
    showFingerColors: Boolean,
    showHints: Boolean
) {
    val engine = viewModel.engine
    val focusRequester = remember { FocusRequester() }
    var showExitDialog by remember { mutableStateOf(false) }
    var showLegendDialog by remember { mutableStateOf(false) }
    var hiddenInputText by remember { mutableStateOf(TextFieldValue("")) }

    BackHandler {
        showExitDialog = true
    }

    // Auto-focus physical keyboard listener on screen enter
    LaunchedEffect(Unit) {
        try {
            focusRequester.requestFocus()
        } catch (_: Exception) {}
    }

    // Format timer
    val timeDisplay = if (engine.testDurationSeconds > 0) {
        val minutes = engine.remainingSeconds / 60
        val seconds = engine.remainingSeconds % 60
        String.format("%02d:%02d", minutes, seconds)
    } else {
        val minutes = engine.elapsedSeconds / 60
        val seconds = engine.elapsedSeconds % 60
        String.format("%02d:%02d", minutes, seconds)
    }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            // Hardware keyboard listener
            .focusRequester(focusRequester)
            .focusable()
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == androidx.compose.ui.input.key.KeyEventType.KeyDown) {
                    val nativeEvent = keyEvent.nativeKeyEvent
                    when (nativeEvent.keyCode) {
                        KeyEvent.KEYCODE_DEL -> {
                            viewModel.onBackspace()
                            true
                        }
                        KeyEvent.KEYCODE_ENTER -> {
                            viewModel.onKeyTyped('\n')
                            true
                        }
                        KeyEvent.KEYCODE_SPACE -> {
                            viewModel.onKeyTyped(' ')
                            true
                        }
                        else -> {
                            val unicodeChar = nativeEvent.unicodeChar
                            if (unicodeChar != 0 && unicodeChar >= 32) {
                                viewModel.onKeyTyped(unicodeChar.toChar())
                                true
                            } else {
                                false
                            }
                        }
                    }
                } else {
                    false
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { showExitDialog = true },
                    modifier = Modifier.testTag("btn_practice_back")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Exit",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = viewModel.activeSessionTitle.ifEmpty { "Practice Session" },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1
                    )
                    Text(
                        text = if (engine.isRunning) {
                            if (isHindi) "● अभ्यास चालू है" else "● Practice in progress"
                        } else {
                            if (isHindi) "टाइप करना शुरू करें..." else "Start typing to begin..."
                        },
                        fontSize = 11.sp,
                        color = if (engine.isRunning) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = { showLegendDialog = true },
                    modifier = Modifier.testTag("btn_practice_legend")
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Finger Legend",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // No-Backspace Exam Mode Alert Banner
            if (!engine.allowBackspace) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFFEF2F2),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("banner_no_backspace")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFDC2626)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "✕", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isHindi) "⚠️ परीक्षा नियम: बैकस्पेस पूर्णतः बंद है (0 सुधार)" else "⚠️ EXAM RULE: NO BACKSPACE ALLOWED (BSF HCM Mode)",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp,
                                color = Color(0xFFB91C1C)
                            )
                            Text(
                                text = if (isHindi)
                                    "गलती सुधारने की अनुमति नहीं है। रुकावटें: ${engine.blockedBackspaceCount}"
                                else
                                    "Corrections disallowed under force exam rules. Blocked attempts: ${engine.blockedBackspaceCount}",
                                fontSize = 10.sp,
                                color = Color(0xFF991B1B)
                            )
                        }
                    }
                }
            }

            // 1. Live Stats Bar (Timer, WPM, Accuracy, Errors)
            LiveStatsBar(
                timeDisplay = timeDisplay,
                grossWpm = engine.grossWpm,
                netWpm = engine.netWpm,
                accuracy = engine.accuracy,
                errors = engine.incorrectKeystrokes,
                isRunning = engine.isRunning,
                isHindi = isHindi,
                onTogglePause = { viewModel.togglePause() },
                onRestart = { viewModel.restartSession() }
            )

            // 2. Practice Text View (Syntax highlighted + cursor)
            PracticeTextView(
                targetText = engine.targetText,
                currentIndex = engine.currentIndex,
                charStates = engine.charStates,
                isHindi = isHindi,
                focusRequester = focusRequester,
                onPhysicalChar = { viewModel.onKeyTyped(it) },
                onBackspace = { viewModel.onBackspace() }
            )

            // Hidden BasicTextField for Android soft keyboard compatibility
            BasicTextField(
                value = hiddenInputText,
                onValueChange = { newVal ->
                    if (newVal.text.isNotEmpty()) {
                        val typedChar = newVal.text.last()
                        viewModel.onKeyTyped(typedChar)
                    }
                    hiddenInputText = TextFieldValue("")
                },
                modifier = Modifier
                    .size(1.dp)
                    .alpha(0f)
            )

            // 3. Visual Finger Placement Guide (Target key, Finger name, Hands Visualizer)
            FingerPlacementGuide(
                targetChar = engine.currentTargetChar,
                language = if (isHindi) "hi" else "en",
                showLegend = false
            )

            // 4. On-Screen QWERTY Keyboard
            OnScreenKeyboard(
                targetChar = engine.currentTargetChar,
                showFingerColors = showFingerColors,
                showHints = showHints,
                allowBackspace = engine.allowBackspace,
                onKeyTapped = { char ->
                    viewModel.onKeyTyped(char)
                },
                onBackspaceTapped = {
                    viewModel.onBackspace()
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Exit confirmation dialog
        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = { showExitDialog = false },
                title = {
                    Text(text = if (isHindi) "अभ्यास छोड़ें?" else "Exit Practice?")
                },
                text = {
                    Text(
                        text = if (isHindi)
                            "क्या आप वाकई अभ्यास से बाहर जाना चाहते हैं? वर्तमान प्रगति सेव नहीं होगी।"
                        else
                            "Are you sure you want to exit? Your current test progress will be lost."
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showExitDialog = false
                            viewModel.navigateTo(Screen.HOME)
                        }
                    ) {
                        Text(text = if (isHindi) "हाँ, बाहर जाएं" else "Yes, Exit")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showExitDialog = false }) {
                        Text(text = if (isHindi) "रद्द करें" else "Cancel")
                    }
                }
            )
        }

        // Finger Color Legend Dialog
        if (showLegendDialog) {
            AlertDialog(
                onDismissRequest = { showLegendDialog = false },
                title = {
                    Text(text = if (isHindi) "उंगली रंग निर्देशिका" else "Finger Placement Guide")
                },
                text = {
                    com.example.ui.components.FingerColorLegend(isHindi = isHindi)
                },
                confirmButton = {
                    Button(onClick = { showLegendDialog = false }) {
                        Text(text = if (isHindi) "ठीक है" else "Got it")
                    }
                }
            )
        }
    }
}
