package com.testbuddy.com.testbuddy.models

import com.testbuddy.models.TestingChecklistNode

/**
 * Custom user object which gets passed into checkListTree which contains useful information used by the renderer.
 *
 * @param checklistNode Information regarding the node.
 */
data class ChecklistUserObject(var checklistNode: TestingChecklistNode)
