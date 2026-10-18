package com.example.swpp.tododapp.taskdetail

import androidx.test.filters.MediumTest
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.swpp.tododapp.R
import com.example.swpp.tododapp.data.Task
import org.junit.Test
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.swpp.tododapp.ServiceLocator
import com.example.swpp.tododapp.data.source.FakeAndroidTestRepository
import com.example.swpp.tododapp.data.source.TasksRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import kotlinx.coroutines.test.runTest
import org.hamcrest.core.IsNot.not


@MediumTest
@RunWith(AndroidJUnit4::class)
class TaskDetailFragmentTest {
    private lateinit var repository: TasksRepository

    @Before
    fun initRepository() {
        repository = FakeAndroidTestRepository()
        ServiceLocator.tasksRepository = repository
    }

    @After
    fun cleanupDb() = runTest {
        ServiceLocator.resetRepository()
    }

    @Test
    fun activeTaskDetails_launched_displaysTitleDescriptionAndUncheckedStatus() = runTest {
        // Given an active task in the repository.
        val activeTask = Task("Active Task", "AndroidX Rocks", false, "active-detail-1")
        repository.saveTask(activeTask)

        // When the details fragment is launched for that task.
        val bundle = TaskDetailFragmentArgs(activeTask.id).toBundle()
        launchFragmentInContainer<TaskDetailFragment>(bundle, R.style.AppTheme)

        // Then the title, description, and active completion state are displayed.
        onView(withId(R.id.task_detail_title_text))
            .check(matches(isDisplayed()))
            .check(matches(withText("Active Task")))
        onView(withId(R.id.task_detail_description_text))
            .check(matches(isDisplayed()))
            .check(matches(withText("AndroidX Rocks")))
        onView(withId(R.id.task_detail_complete_checkbox))
            .check(matches(isDisplayed()))
            .check(matches(not(isChecked())))
    }
}
