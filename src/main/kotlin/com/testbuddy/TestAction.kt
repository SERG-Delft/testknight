package com.testbuddy

import com.intellij.coverage.CoverageDataManager
import com.intellij.coverage.CoverageRunner
import com.intellij.coverage.JavaCoverageEngine
import com.intellij.coverage.JavaCoverageRunner
import com.intellij.execution.RunManager
import com.intellij.execution.testframework.JavaTestLocator
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys

class TestAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val psiFile = event.getData(CommonDataKeys.PSI_FILE)!!
        val editor = event.getData(CommonDataKeys.EDITOR)!!
        val project = event.getData(CommonDataKeys.PROJECT)!!

        val jloc = JavaTestLocator.INSTANCE
        val runMgr = RunManager.getInstance(project)
        val covDataMgr = CoverageDataManager.getInstance(project)

        val covEngine = covDataMgr.currentSuitesBundle.coverageEngine

        val covRunner = CoverageRunner.getInstance(JavaCoverageRunner::class.java)

        covEngine.getTestsForLine(project, "Point", 10)
    }
}
