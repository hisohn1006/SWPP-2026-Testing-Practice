package com.example.swpp.tododapp.statistics

import com.example.swpp.tododapp.data.Task
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class StatisticsUtilsTest(
    private val caseName: String,
    private val tasks: List<Task>,
    private val expectedActive: Float,
    private val expectedCompleted: Float
) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): List<Array<Any>> = listOf(
            arrayOf(
                "one active and one completed",
                listOf(Task("Active", "desc", false, "active-1"), Task("Done", "desc", true, "done-1")),
                50f, 50f
            ),
            arrayOf("empty list", emptyList<Task>(), 0f, 0f),
            arrayOf(
                "two completed",
                listOf(Task("Done 1", "desc", true, "done-1"), Task("Done 2", "desc", true, "done-2")),
                0f, 100f
            )
        )
    }

    @Test
    fun getActiveAndCompletedStats_threeInputs_returnsExpectedPercentages() {
        // Given the named task list and expected percentages.
        // When statistics are calculated.
        val result = getActiveAndCompletedStats(tasks)
        // Then both percentages match this case's oracle.
        assertEquals(caseName, expectedActive, result.activeTasksPercent)
        assertEquals(caseName, expectedCompleted, result.completedTasksPercent)
    }
}
