package com.example.swpp.tododapp.tasks

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.swpp.tododapp.R
import com.example.swpp.tododapp.ServiceLocator
import com.example.swpp.tododapp.data.Task
import com.example.swpp.tododapp.data.source.FakeAndroidTestRepository
import com.example.swpp.tododapp.data.source.TasksRepository
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@MediumTest
class TasksFragmentTest {
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
    fun clickTask_firstItem_navigatesToItsDetail() = runTest {
        // Given two tasks on the home screen and a mock navigation controller.
        repository.saveTask(Task("TITLE1", "DESCRIPTION1", false, "id1"))
        repository.saveTask(Task("TITLE2", "DESCRIPTION2", true, "id2"))
        val scenario = launchFragmentInContainer<TasksFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)
        scenario.onFragment { Navigation.setViewNavController(it.requireView(), navController) }

        // When the row titled TITLE1 is clicked.
        onView(withId(R.id.tasks_list)).perform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                hasDescendant(withText("TITLE1")), click()
            )
        )

        // Then navigation targets that task's detail screen.
        verify(navController).navigate(
            TasksFragmentDirections.actionTasksFragmentToTaskDetailFragment("id1")
        )
    }
}
