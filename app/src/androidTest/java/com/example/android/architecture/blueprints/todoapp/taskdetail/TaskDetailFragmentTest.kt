package com.example.android.architecture.blueprints.todoapp.taskdetail

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.data.Task
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
@MediumTest
@RunWith(AndroidJUnit4::class)
class TaskDetailFragmentTest{

    @Test
    fun activeTaskDetails_DisplayedInUi() {
        // GIVEN - Add active (incomplete) task to the DB
        //Creating a task.
        val activeTask = Task("Active Task", "AndroidX Rocks", false)
   //     Creating a Bundle, which represents the fragment arguments for the task that get passed into the fragment).
        // WHEN - Details fragment launched to display task
        val bundle = TaskDetailFragmentArgs(activeTask.id).toBundle()
//The launchFragmentInContainer function creates a FragmentScenario, with this bundle and a theme.
        launchFragmentInContainer<TaskDetailFragment>(bundle, R.style.AppTheme)
      //  Thread.sleep(2000)

    }

}