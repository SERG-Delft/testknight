package com.testknight.services

import com.testknight.extensions.TestKnightTestCase
import junit.framework.TestCase
import org.junit.Test

internal class TestAnalyzerServiceTest : TestKnightTestCase() {

    @Test
    fun testIsTestClassTrue() {
        val data = getBasicTestInfo("/PointTest.java")
        val testAnalyzerService = TestAnalyzerService()
        TestCase.assertTrue(testAnalyzerService.isTestClass(data.psiClass!!))
    }

    @Test
    fun testIsTestClassFalse() {
        val data = getBasicTestInfo("/Person.java")
        val testAnalyzerService = TestAnalyzerService()
        TestCase.assertFalse(testAnalyzerService.isTestClass(data.psiClass!!))
    }

    @Test
    fun testIsTestClassEmptyClass() {
        val data = getBasicTestInfo("/EmptyClass.java")
        val testAnalyzerService = TestAnalyzerService()
        TestCase.assertFalse(testAnalyzerService.isTestClass(data.psiClass!!))
    }
}
