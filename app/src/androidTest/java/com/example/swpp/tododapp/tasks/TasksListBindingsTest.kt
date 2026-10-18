package com.example.swpp.tododapp.tasks

import android.view.View
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.swpp.tododapp.data.Task
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class TasksListBindingsTest {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()

    private fun task(id: String, completed: Boolean, due: String?) = Task(
        title = id,
        description = "description",
        isCompleted = completed,
        id = id,
        dueDate = due?.let(LocalDate::parse)
    )

    @Test fun setDueDateLabel_completedWithDate_showsClear() {
        instrumentation.runOnMainSync {
            // Given a fresh view and a completed task due on 2026-10-20.
            val view = TextView(instrumentation.targetContext)
            val item = task("done-1", true, "2026-10-20")
            // When the task is bound.
            setDueDateLabel(view, item)
            // Then CLEAR is visible regardless of today's date.
            assertEquals("CLEAR", view.text.toString())
            assertEquals(View.VISIBLE, view.visibility)
        }
    }

    @Test fun setDueDateLabel_completedWithoutDate_showsClear() {
        instrumentation.runOnMainSync {
            // Given a fresh view and a completed task with no due date.
            val view = TextView(instrumentation.targetContext)
            val item = task("done-2", true, null)
            // When the task is bound.
            setDueDateLabel(view, item)
            // Then CLEAR is visible.
            assertEquals("CLEAR", view.text.toString())
            assertEquals(View.VISIBLE, view.visibility)
        }
    }

    @Test fun setDueDateLabel_reboundActiveWithoutDate_clearsAndHides() {
        instrumentation.runOnMainSync {
            // Given a view previously bound to completed task done-2.
            val view = TextView(instrumentation.targetContext)
            setDueDateLabel(view, task("done-2", true, null))
            // When it is rebound to active task active-1 with no due date.
            setDueDateLabel(view, task("active-1", false, null))
            // Then the former CLEAR label is erased and hidden.
            assertEquals("", view.text.toString())
            assertEquals(View.GONE, view.visibility)
        }
    }

    @Test fun setDueDateLabel_reboundNull_clearsAndHides() {
        instrumentation.runOnMainSync {
            // Given a view previously bound to completed task done-2.
            val view = TextView(instrumentation.targetContext)
            setDueDateLabel(view, task("done-2", true, null))
            // When it is rebound with no task.
            setDueDateLabel(view, null)
            // Then the former CLEAR label is erased and hidden.
            assertEquals("", view.text.toString())
            assertEquals(View.GONE, view.visibility)
        }
    }

    @Test fun setDueDateLabel_freshActiveWithoutDate_hidesEmptyLabel() {
        instrumentation.runOnMainSync {
            // Given a fresh view and active task active-1 without a due date.
            val view = TextView(instrumentation.targetContext)
            // When the task is bound.
            setDueDateLabel(view, task("active-1", false, null))
            // Then the empty label is hidden.
            assertEquals("", view.text.toString())
            assertEquals(View.GONE, view.visibility)
        }
    }

    @Test fun setDueDateLabel_freshNull_hidesEmptyLabel() {
        instrumentation.runOnMainSync {
            // Given a fresh view.
            val view = TextView(instrumentation.targetContext)
            // When a null task is bound.
            setDueDateLabel(view, null)
            // Then the empty label is hidden.
            assertEquals("", view.text.toString())
            assertEquals(View.GONE, view.visibility)
        }
    }
}
