package com.example

import com.example.engine.TypingEngine
import com.example.model.Finger
import com.example.model.WeakKeyHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TypingEngineTest {

    @Test
    fun testFingerMappingHomeRow() {
        assertEquals(Finger.LEFT_PINKY, Finger.getFingerForChar('a'))
        assertEquals(Finger.LEFT_RING, Finger.getFingerForChar('s'))
        assertEquals(Finger.LEFT_MIDDLE, Finger.getFingerForChar('d'))
        assertEquals(Finger.LEFT_INDEX, Finger.getFingerForChar('f'))

        assertEquals(Finger.RIGHT_INDEX, Finger.getFingerForChar('j'))
        assertEquals(Finger.RIGHT_MIDDLE, Finger.getFingerForChar('k'))
        assertEquals(Finger.RIGHT_RING, Finger.getFingerForChar('l'))
        assertEquals(Finger.RIGHT_PINKY, Finger.getFingerForChar(';'))

        assertEquals(Finger.THUMBS, Finger.getFingerForChar(' '))
    }

    @Test
    fun testTypingEngineCorrectTyping() {
        val engine = TypingEngine()
        engine.loadExercise("asdf", 0)

        assertEquals('a', engine.currentTargetChar)
        assertEquals(Finger.LEFT_PINKY, engine.currentTargetFinger)

        val matchA = engine.processChar('a')
        assertTrue(matchA == true)
        assertEquals(1, engine.correctKeystrokes)
        assertEquals(0, engine.incorrectKeystrokes)
        assertEquals('s', engine.currentTargetChar)

        val matchS = engine.processChar('s')
        assertTrue(matchS == true)

        val matchD = engine.processChar('d')
        assertTrue(matchD == true)

        val matchF = engine.processChar('f')
        assertTrue(matchF == true)

        assertTrue(engine.isFinished)
        assertEquals(100f, engine.accuracy, 0.01f)
    }

    @Test
    fun testTypingEngineMistakeRecording() {
        val engine = TypingEngine()
        engine.loadExercise("dad", 0)

        // Expected 'd', typed 'f'
        val result = engine.processChar('f')
        assertFalse(result == true)
        assertEquals(1, engine.incorrectKeystrokes)
        assertEquals(1, engine.mistypedKeys['D'])
        assertEquals(1, engine.errorLog.size)
        assertEquals('d', engine.errorLog[0].expected)
        assertEquals('f', engine.errorLog[0].pressed)

        // Next character is 'a', type correctly
        engine.processChar('a')
        // Next character is 'd', type correctly
        engine.processChar('d')

        assertTrue(engine.isFinished)
        assertEquals(3, engine.totalKeystrokes)
        assertEquals(2, engine.correctKeystrokes)
        // Accuracy should be (2/3) * 100 = 66.67%
        assertEquals(66.67f, engine.accuracy, 0.1f)
    }

    @Test
    fun testWeakKeyDrillGeneration() {
        val drill = WeakKeyHelper.generateDrillForWeakKeys(listOf('d', 'k'), wordCount = 10)
        assertNotNull(drill)
        assertTrue(drill.isNotEmpty())
        val words = drill.split(" ")
        assertEquals(10, words.size)
    }

    @Test
    fun testBsfHcmNoBackspaceRule() {
        val engine = TypingEngine()
        // Load with allowBackspaceOption = false for BSF HCM
        engine.loadExercise("bsf", 0, allowBackspaceOption = false, examMode = "BSF_HCM")
        assertFalse(engine.allowBackspace)

        // Type 'b' correctly
        engine.processChar('b')
        assertEquals(1, engine.currentIndex)

        // Try backspace - should be rejected!
        val backspaceResult = engine.processBackspace()
        assertFalse(backspaceResult)
        assertEquals(1, engine.blockedBackspaceCount)
        // Cursor index should NOT move back
        assertEquals(1, engine.currentIndex)
    }

    @Test
    fun testExamParagraphSearch() {
        val bsfResults = com.example.model.ExamParagraphCatalog.search("BSF", "ALL")
        assertTrue(bsfResults.isNotEmpty())
        assertTrue(bsfResults.any { it.examTag == "BSF HCM" })
        assertFalse(bsfResults.first { it.examTag == "BSF HCM" }.allowBackspace)

        val sscResults = com.example.model.ExamParagraphCatalog.search("CGL", "ALL")
        assertTrue(sscResults.isNotEmpty())
        assertTrue(sscResults.any { it.examTag == "SSC CGL" })

        val courtResults = com.example.model.ExamParagraphCatalog.search("Court", "COURT")
        assertTrue(courtResults.isNotEmpty())
    }
}
