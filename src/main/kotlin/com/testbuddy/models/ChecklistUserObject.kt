package com.testbuddy.models

/**
 * Custom user object which gets passed into checkListTree which contains useful information used by the renderer.
 *
 * @param checklistNode Information regarding the node.
 * @param checkCount Number of checked items under the node.
 */
data class ChecklistUserObject(var checklistNode: TestingChecklistNode, var checkCount: Int)
