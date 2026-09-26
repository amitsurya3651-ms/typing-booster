package com.example

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.TestSelectionDialog
import com.example.ui.screens.ExamParagraphsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LessonsScreen
import com.example.ui.screens.PracticeScreen
import com.example.ui.screens.ProgressScreen
import com.example.ui.screens.ResultScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.WeakKeysScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.Screen
import com.example.viewmodel.TypingViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: TypingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
            val testResults by viewModel.testResults.collectAsStateWithLifecycle()
            val keyStats by viewModel.keyStats.collectAsStateWithLifecycle()
            val achievements by viewModel.achievements.collectAsStateWithLifecycle()

            val isHindi = userProfile?.language == "hi"
            val showFingerColors = userProfile?.fingerColorsEnabled ?: true
            val showHints = userProfile?.keyboardHintsEnabled ?: true

            var showTestDialog by remember { mutableStateOf(false) }

            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (viewModel.currentScreen) {
                            Screen.HOME -> {
                                HomeScreen(
                                    viewModel = viewModel,
                                    userProfile = userProfile,
                                    onOpenTestDialog = { showTestDialog = true }
                                )
                            }
                            Screen.LESSONS -> {
                                LessonsScreen(
                                    viewModel = viewModel,
                                    lessonsCompleted = userProfile?.lessonsCompleted ?: 0,
                                    isHindi = isHindi
                                )
                            }
                            Screen.EXAM_PARAGRAPHS -> {
                                ExamParagraphsScreen(
                                    viewModel = viewModel,
                                    isHindi = isHindi
                                )
                            }
                            Screen.PRACTICE -> {
                                PracticeScreen(
                                    viewModel = viewModel,
                                    isHindi = isHindi,
                                    showFingerColors = showFingerColors,
                                    showHints = showHints
                                )
                            }
                            Screen.RESULT -> {
                                ResultScreen(
                                    viewModel = viewModel,
                                    latestResult = viewModel.latestResult,
                                    previousResult = viewModel.previousResult,
                                    isHindi = isHindi
                                )
                            }
                            Screen.WEAK_KEYS -> {
                                WeakKeysScreen(
                                    viewModel = viewModel,
                                    keyStats = keyStats,
                                    isHindi = isHindi
                                )
                            }
                            Screen.PROGRESS -> {
                                ProgressScreen(
                                    viewModel = viewModel,
                                    userProfile = userProfile,
                                    testResults = testResults,
                                    achievements = achievements,
                                    isHindi = isHindi
                                )
                            }
                            Screen.SETTINGS -> {
                                SettingsScreen(
                                    viewModel = viewModel,
                                    userProfile = userProfile,
                                    isHindi = isHindi
                                )
                            }
                        }

                        if (showTestDialog) {
                            TestSelectionDialog(
                                isHindi = isHindi,
                                onDismiss = { showTestDialog = false },
                                onBrowseExamPassages = {
                                    showTestDialog = false
                                    viewModel.navigateTo(Screen.EXAM_PARAGRAPHS)
                                },
                                onStartTest = { duration, title, passage, allowBackspace ->
                                    showTestDialog = false
                                    viewModel.startTimedTest(duration, title, passage, allowBackspace)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (viewModel.currentScreen == Screen.PRACTICE && event != null) {
            when (keyCode) {
                KeyEvent.KEYCODE_DEL -> {
                    viewModel.onBackspace()
                    return true
                }
                KeyEvent.KEYCODE_ENTER -> {
                    viewModel.onKeyTyped('\n')
                    return true
                }
                KeyEvent.KEYCODE_SPACE -> {
                    viewModel.onKeyTyped(' ')
                    return true
                }
                else -> {
                    val unicode = event.unicodeChar
                    if (unicode >= 32) {
                        viewModel.onKeyTyped(unicode.toChar())
                        return true
                    }
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}
