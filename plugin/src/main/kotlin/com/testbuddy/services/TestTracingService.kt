package com.testbuddy.services

import com.intellij.coverage.CoverageDataManager
import com.intellij.coverage.CoverageSuitesBundle
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.markup.HighlighterLayer
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtilRt
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.ui.ColorUtil
import com.testbuddy.GlobalHighlighter
import com.testbuddy.exceptions.NoTestCoverageDataException
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
    private val exceptionHandlerService = project.service<ExceptionHandlerService>()

    /**
     * The current coverage data to show.
     */
    private var activeCovData: TestCoverageData? = null

    /**
     * Highlight the lines covered by testName.
     *
     * @param testName the string representation of the test.
     */
    fun highlightTest(testName: String) {
        activeCovData = getLinesForTest(testName)
        rebuildHighlights()
    }

    /**
     * Highlight the lines covered within an editor.
     *
     * @param editor the editor
     */
    override fun highlightEditor(editor: Editor) {

        activeCovData ?: return

        val psiFile = psiDocumentManager.getPsiFile(editor.document)
        val classQn = PsiTreeUtil.findChildOfType(psiFile, PsiClass::class.java)?.qualifiedName

        if (psiFile == null || classQn == null) return

        // get the coverage data
        val lines = activeCovData!!.classes[classQn] ?: return

        val textAttributes = TextAttributes()
        textAttributes.backgroundColor = ColorUtil.fromHex(SettingsService.instance.state.coverageSettings.tracedColor)

        // highlight each line
        lines.forEach {
            highlighters.add(
                editor.markupModel.addLineHighlighter(
                    it - 1,
                    HighlighterLayer.ADDITIONAL_SYNTAX,
                    textAttributes
                )
            )
        }
    }

    /**
     * Get the lines of code tested by a given test.
     *
     * @param test the test in string format: ClassName,testName.
     * @return a TestCoverageObject representing the lines covered by the test.
     */
    @Throws(NoTestCoverageDataException::class)
    @Suppress("TooGenericExceptionCaught")
    private fun getLinesForTest(test: String): TestCoverageData? {

        try {
            val currentSuitesBundle = coverageDataManager.currentSuitesBundle
            val traceFile = getTraceFile(test, currentSuitesBundle)
            return readTraceFile(traceFile)
        } catch (ex: FileNotFoundException) {
            exceptionHandlerService.notify(
                "Test coverage info not found",
                "Make sure you have ran with coverage and test-tracing", NotificationType.ERROR
            )
            println(ex)
        } catch (ex: IllegalStateException) {
            exceptionHandlerService.notify(
                "Test coverage info not found",
                "Make sure you have ran with coverage and test-tracing", NotificationType.ERROR
            )
            println(ex)
        } catch (ex: IOException) {
            exceptionHandlerService.notify(
                "Failed to read trace file",
                "Rerun coverage", NotificationType.ERROR
            )
            println(ex)
        } catch (ex: NullPointerException) {
            exceptionHandlerService.notify(
                "Failed to read trace file",
                "Not coverage suit found", NotificationType.ERROR
            )
            println(ex)
        }

        throw NoTestCoverageDataException()
    }

    /**
     * Gets the trace file given a coverage suite and testName.
     *
     * @param test the test.
     * @param coverageSuitesBundle the coverage suite to extract the data from.
     * @return the file pointer of the trace file.
     */
    @Throws(FileNotFoundException::class)
    private fun getTraceFile(test: String, coverageSuitesBundle: CoverageSuitesBundle): File {

        val traceDirs = coverageSuitesBundle.suites.map {
            val filePath = it.coverageDataFileName
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

        throw FileNotFoundException("no trace file found")
    }

    /**
     * Read a trace file and extract the information into a TestCoverageData object.
     *
     * @param traceFile the traceFile.
     * @return the lines of code covered by the test.
     */
    @Suppress("TooGenericExceptionCaught")
    @Throws(IOException::class)
    fun readTraceFile(traceFile: File): TestCoverageData {

        try {
            val coverage = TestCoverageData(traceFile.nameWithoutExtension)
            val stream = DataInputStream(FileInputStream(traceFile))
            val numClasses = stream.readInt()

            repeat(numClasses) {

                val className = stream.readUTF()
                val linesSize = stream.readInt()

                coverage.classes[className] = mutableListOf()

                repeat(linesSize) {
                    val line = stream.readInt()
                    (coverage.classes[className] as MutableList).add(line)
                }
            }
            stream.close()
            return coverage
        } catch (ex: IOException) {

            println("tracefile could not be read" + ex.message)
            throw ex
        }
    }
}
