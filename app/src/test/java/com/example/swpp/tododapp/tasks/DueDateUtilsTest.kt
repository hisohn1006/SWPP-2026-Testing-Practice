package com.example.swpp.tododapp.tasks

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class DueDateUtilsTest {
    private fun date(value: String) = LocalDate.parse(value)

    @Test fun dueDateLabel_completedWithDate_returnsClear() {
        // Given a completed task due on 2026-10-20.
        val due = date("2026-10-20")
        // When its label is built on 2026-10-19.
        val result = dueDateLabel(due, date("2026-10-19"), true)
        // Then completion takes precedence.
        assertEquals("CLEAR", result)
    }

    @Test fun dueDateLabel_completedWithoutDate_returnsClear() {
        // Given a completed task without a due date.
        // When its label is built on 2026-10-19.
        val result = dueDateLabel(null, date("2026-10-19"), true)
        // Then completion takes precedence.
        assertEquals("CLEAR", result)
    }

    @Test fun dueDateLabel_activeWithoutDate_returnsEmpty() {
        // Given an active task without a due date.
        // When its label is built on 2026-10-19.
        val result = dueDateLabel(null, date("2026-10-19"), false)
        // Then no label is shown.
        assertEquals("", result)
    }

    @Test fun dueDateLabel_threeDaysWithinMonth_returnsExactLabels() {
        // Given active tasks three days ahead of and behind 2026-10-19.
        val today = date("2026-10-19")
        // When both labels are built.
        val future = dueDateLabel(date("2026-10-22"), today, false)
        val past = dueDateLabel(date("2026-10-16"), today, false)
        // Then direction and distance are exact.
        assertEquals("D-3", future)
        assertEquals("D+3", past)
    }

    @Test fun dueDateLabel_dueToday_returnsDDay() {
        // Given an active task due on 2026-10-19.
        // When its label is built on that date.
        val result = dueDateLabel(date("2026-10-19"), date("2026-10-19"), false)
        // Then zero distance is D-Day.
        assertEquals("D-Day", result)
    }

    @Test fun dueDateLabel_dueTomorrow_returnsDMinus1() {
        // Given an active task due on 2026-10-20.
        // When its label is built on 2026-10-19.
        val result = dueDateLabel(date("2026-10-20"), date("2026-10-19"), false)
        // Then the result is D-1.
        assertEquals("D-1", result)
    }

    @Test fun dueDateLabel_overdueYesterday_returnsDPlus1() {
        // Given an active task due on 2026-10-18.
        // When its label is built on 2026-10-19.
        val result = dueDateLabel(date("2026-10-18"), date("2026-10-19"), false)
        // Then the result is D+1.
        assertEquals("D+1", result)
    }

    @Test fun dueDateLabel_monthBoundaryNeighbors_returnsExactLabels() {
        // Given 2026-10-31 and due dates on each side of it.
        val today = date("2026-10-31")
        // When each active label is built.
        val labels = listOf("2026-10-30", "2026-10-31", "2026-11-01")
            .map { dueDateLabel(date(it), today, false) }
        // Then calendar distance remains continuous.
        assertEquals(listOf("D+1", "D-Day", "D-1"), labels)
    }

    @Test fun dueDateLabel_monthSpanningThreeDays_returnsExactLabels() {
        // Given dates three calendar days apart across October and November.
        // When labels are built in both directions.
        val future = dueDateLabel(date("2026-11-02"), date("2026-10-30"), false)
        val past = dueDateLabel(date("2026-10-30"), date("2026-11-02"), false)
        // Then neither count loses the month component.
        assertEquals("D-3", future)
        assertEquals("D+3", past)
    }

    @Test fun dueDateLabel_fullMonthBoundaryFuture_returnsTotalDays() {
        // Given 2026-10-19 and due dates 30, 31, and 32 days later.
        val today = date("2026-10-19")
        // When active labels are built.
        val labels = listOf("2026-11-18", "2026-11-19", "2026-11-20")
            .map { dueDateLabel(date(it), today, false) }
        // Then the entire month is counted.
        assertEquals(listOf("D-30", "D-31", "D-32"), labels)
    }

    @Test fun dueDateLabel_fullMonthBoundaryPast_returnsTotalDays() {
        // Given a due date of 2026-10-19 and today 30, 31, or 32 days later.
        val due = date("2026-10-19")
        // When active labels are built.
        val labels = listOf("2026-11-18", "2026-11-19", "2026-11-20")
            .map { dueDateLabel(due, date(it), false) }
        // Then the entire overdue duration is counted.
        assertEquals(listOf("D+30", "D+31", "D+32"), labels)
    }

    @Test fun dueDateLabel_leapDayBoundary_returnsExactLabels() {
        // Given leap day 2028-02-29 and its two neighbors.
        val today = date("2028-02-29")
        // When labels are built across the boundary.
        val labels = listOf("2028-02-28", "2028-02-29", "2028-03-01")
            .map { dueDateLabel(date(it), today, false) }
        val spanning = dueDateLabel(date("2028-03-01"), date("2028-02-28"), false)
        // Then the leap day is counted exactly once.
        assertEquals(listOf("D+1", "D-Day", "D-1"), labels)
        assertEquals("D-2", spanning)
    }

    @Test fun dueDateLabel_yearBoundaryNeighbors_returnsExactLabels() {
        // Given 2026-12-31 and its neighboring due dates.
        val today = date("2026-12-31")
        // When active labels are built.
        val labels = listOf("2026-12-30", "2026-12-31", "2027-01-01")
            .map { dueDateLabel(date(it), today, false) }
        // Then crossing the year does not reset the count.
        assertEquals(listOf("D+1", "D-Day", "D-1"), labels)
    }
}
