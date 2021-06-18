package com.testknight.models

import com.testknight.models.testingChecklist.TestingChecklistNode

/**
 * Custom user object which gets passed into checkListTree which contains useful information used by the renderer.
 *
 * @param checklistNode Information regarding the node.
 */
data class ChecklistUserObject(var checklistNode: TestingChecklistNode)
