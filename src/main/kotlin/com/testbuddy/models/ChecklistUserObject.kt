package com.testbuddy.models

/**
 * Custom user object which gets passed into checkListTree which contains useful information used by the renderer.
 *
 * @param checklistNode Information regarding the node.
 */
data class ChecklistUserObject(var checklistNode: TestingChecklistNode)
