package com.testbuddy.services

import com.intellij.coverage.CoverageDataManager
import com.intellij.coverage.CoverageSuitesBundle
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.markup.HighlighterLayer
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtilRt
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.com.testbuddy.models.TestCoverageData
import java.awt.Color
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

class TestTracingService(val project: Project) {

    private val coverageDataManager = CoverageDataManager.getInstance(project)
    private val fileEditorManager = FileEditorManager.getInstance(project)
    private val psiDocumentManager = PsiDocumentManager.getInstance(project)
    private val highlighters = mutableListOf<RangeHighlighter>()

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
        removeHighlights()
        activeCovData = getLinesForTest(testName)
        refreshHighlights()
    }

    /**
     * Highlight all active editors.
     */
    fun refreshHighlights() {

        activeCovData ?: return

        fileEditorManager.allEditors.forEach {
            if (it is TextEditor) {
                highlightEditor(it.editor, activeCovData!!)
            }
        }
    }

    /**
     * Hide all highlighters
     */
    fun removeHighlights() {
        highlighters.forEach { it.dispose() }
        activeCovData = null
    }

    /**
     * Highlight the lines covered within an editor.
     *
     * @param editor the editor
     * @param covData the coverage data
     */
    private fun highlightEditor(editor: Editor, covData: TestCoverageData) {

        val psiFile = psiDocumentManager.getPsiFile(editor.document)
        val classQn = PsiTreeUtil.findChildOfType(psiFile, PsiClass::class.java)?.qualifiedName

        if (psiFile == null || classQn == null) return

        // get the coverage data
        val lines = covData.classes[classQn] ?: return

        val textAttributes = TextAttributes()
        textAttributes.backgroundColor = Color.CYAN

        // highlight each line
        lines.forEach {
            highlighters.add(editor.markupModel.addLineHighlighter(it - 1, HighlighterLayer.LAST, textAttributes))
        }
    }

    /**
     * Get the lines of code tested by a given test.
     *
     * @param test the test in string format: ClassName,testName.
     * @return a TestCoverageObject representing the lines covered by the test.
     */
    private fun getLinesForTest(test: String): TestCoverageData {

        val currentSuitesBundle = coverageDataManager.currentSuitesBundle
            ?: throw FileNotFoundException("no coverage data")

        val traceFile: File = getTraceFile(test, currentSuitesBundle)
            ?: throw FileNotFoundException("traces not found")

        return readTraceFile(traceFile)
    }

    /**
     * Gets the trace file given a coverage suite and testName.
     *
     * @param test the test.
     * @param coverageSuitesBundle the coverage suite to extract the data from.
     * @return the file pointer of the trace file.
     */
    private fun getTraceFile(test: String, coverageSuitesBundle: CoverageSuitesBundle): File? {

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

        return null
    }

    /**
     * Read a trace file and extract the information into a TestCoverageData object.
     *
     * @param traceFile the traceFile.
     * @return the lines of code covered by the test.
     */
    @Suppress("TooGenericExceptionCaught")
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
