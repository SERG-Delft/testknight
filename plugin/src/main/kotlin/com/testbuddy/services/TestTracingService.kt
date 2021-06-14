package com.testbuddy.services

import com.intellij.coverage.CoverageDataManager
import com.intellij.coverage.CoverageSuitesBundle
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.markup.HighlighterLayer
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtilRt
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.ui.ColorUtil
import com.testbuddy.exceptions.CorruptedTraceFileException
import com.testbuddy.exceptions.NoTestCoverageDataException
import com.testbuddy.exceptions.TraceFileNotFoundException
import com.testbuddy.models.TestCoverageData
import com.testbuddy.settings.SettingsService
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

class TestTracingService(val project: Project) : GlobalHighlighter(project) {

    private val coverageDataManager = CoverageDataManager.getInstance(project)
    private val psiDocumentManager = PsiDocumentManager.getInstance(project)
    private val highlightsPerTest = mutableMapOf<String, MutableList<RangeHighlighter>>()

    /**
     * The current coverage data to show.
     */
    var activeCovData: TestCoverageData? = null

    /**
     * Highlight the lines covered by testName.
     *
     * @param testName the string representation of the test.
     * @throws TraceFileNotFoundException
     * @throws CorruptedTraceFileException
     */
    fun highlightTest(testName: String) {
        activeCovData = getLinesForTest(testName)
        rebuildHighlights()
    }

    /**
     * Un-highlight the lines covered by testName.
     *
     * @param testName the string representation of the test.
     */
    fun removeHighlightsForTest(testName: String) {
        val highlightsForTest = highlightsPerTest[testName]
        highlightsForTest ?: return

        highlightsForTest.forEach { it.dispose() }
    }

    /**
     * Highlight the lines covered within an editor.
     *
     * @param editor the editor
     * @return a list of the added highlighters
     */
    override fun highlightEditor(editor: Editor): List<RangeHighlighter> {

        activeCovData ?: return listOf()

        val psiFile = psiDocumentManager.getPsiFile(editor.document)
        val classQn = PsiTreeUtil.findChildOfType(psiFile, PsiClass::class.java)?.qualifiedName

        if (psiFile == null || classQn == null) return listOf()

        // get the coverage data
        val lines = activeCovData!!.classes[classQn] ?: return listOf()

        val textAttributes = TextAttributes()
        textAttributes.backgroundColor = ColorUtil.fromHex(SettingsService.instance.state.coverageSettings.tracedColor)

        val hls = mutableListOf<RangeHighlighter>()

        // highlight each line
        lines.forEach {
            hls.add(
                editor.markupModel.addLineHighlighter(
                    it - 1,
                    HighlighterLayer.ADDITIONAL_SYNTAX,
                    textAttributes
                )
            )
        }

        // keep track of which highlights belong to each test
        if (highlightsPerTest[activeCovData!!.testName] == null) {
            highlightsPerTest[activeCovData!!.testName] = mutableListOf()
        }
        highlightsPerTest[activeCovData!!.testName]!!.addAll(hls)

        return hls
    }

    /**
     * Get the lines of code tested by a given test.
     *
     * @param test the test in string format: ClassName,testName.
     * @return a TestCoverageObject representing the lines covered by the test.
     * @throws TraceFileNotFoundException
     * @throws CorruptedTraceFileException
     */
    private fun getLinesForTest(test: String): TestCoverageData {
        val coverageSuite = coverageDataManager.suites ?: throw NoTestCoverageDataException()
        val currentSuitesBundle = CoverageSuitesBundle(coverageSuite)
        val traceFile = getTraceFile(test, currentSuitesBundle)
        return readTraceFile(traceFile)
    }

    /**
     * Gets the trace file given a coverage suite and testName.
     *
     * @param test the test.
     * @param coverageSuitesBundle the coverage suite to extract the data from.
     * @return the file pointer of the trace file.
     * @throws TraceFileNotFoundException
     */
    @Throws(FileNotFoundException::class)
    private fun getTraceFile(test: String, coverageSuitesBundle: CoverageSuitesBundle): File {

        val traceDirs = coverageSuitesBundle.suites.map { suite ->
            val filePath = suite.coverageDataFileName
            val dirName = FileUtilRt.getNameWithoutExtension(File(filePath).name)
            val parentDir = File(filePath).parentFile
            File(parentDir, dirName)
        }

        traceDirs
            .sortedByDescending { it.lastModified() }
            .forEach { dir ->

                dir.listFiles()?.forEach { file ->
                    if (file.name == "$test.tr") {
                        return file
                    }
                }
            }

        // if the exception is not found throw an exception
        throw TraceFileNotFoundException()
    }

    /**
     * Read a trace file and extract the information into a TestCoverageData object.
     *
     * @param traceFile the traceFile.
     * @return the lines of code covered by the test.
     * @throws CorruptedTraceFileException
     */
    @Throws(CorruptedTraceFileException::class)
    fun readTraceFile(traceFile: File): TestCoverageData {

        val coverage = TestCoverageData(traceFile.nameWithoutExtension)
        val stream = DataInputStream(FileInputStream(traceFile))
        val numClasses = stream.readInt()

        try {

            // foreach class
            repeat(numClasses) {

                val className = stream.readUTF()
                val linesSize = stream.readInt()

                coverage.classes[className] = mutableListOf()

                // for each line in the class
                repeat(linesSize) {
                    val line = stream.readInt()
                    coverage.classes[className]!!.add(line)
                }
            }
        } catch (ex: IOException) {
            throw CorruptedTraceFileException(ex)
        } finally {
            stream.close()
        }

        return coverage
    }
}
