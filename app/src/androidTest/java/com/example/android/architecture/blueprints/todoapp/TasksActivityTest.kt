package com.example.android.architecture.blueprints.todoapp

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.tasks.TasksActivity
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)//because you're using AndroidX test code.
@LargeTest//signifies these are end-to-end tests
class TasksActivityTest {

    private lateinit var repository: TasksRepository

    @Before
    fun init() {
        //initialize the repository
        repository = ServiceLocator.provideTasksRepository(getApplicationContext())
        runBlocking {
            //delete all the tasks in the repository, to ensure it's completely cleared out
            // before each and every test run.
            repository.deleteAllTasks()
        }
    }

    @After
    fun reset() {
        ServiceLocator.resetRepository()
    }


    @Test
    fun editTask() = runBlocking {
        // Set initial state.
        repository.saveTask(Task("TITLE1", "DESCRIPTION"))

        // Start up Tasks screen.
        //there is an ActivityScenarioRule which calls launch and close for you.
   /*     any setup of the data state, such as adding tasks to the repository, must happen before
        ActivityScenario.launch() is called. Calling such additional setup code, such as saving
        tasks to the repository, is not currently supported by ActivityScenarioRule. Therefore,
        we choose not to use ActivityScenarioRule and instead manually call launch and close.*/
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)

        // Espresso code will go here.
        // Click on the task on the list and verify that all the data is correct.
        onView(withText("TITLE1")).perform(click())
        onView(withId(R.id.task_detail_title_text)).check(matches(withText("TITLE1")))
        onView(withId(R.id.task_detail_description_text)).check(matches(withText("DESCRIPTION")))
        onView(withId(R.id.task_detail_complete_checkbox)).check(matches(not(isChecked())))

        // Click on the edit button, edit, and save.
        onView(withId(R.id.edit_task_fab)).perform(click())
        onView(withId(R.id.add_task_title_edit_text)).perform(replaceText("NEW TITLE"))
        onView(withId(R.id.add_task_description_edit_text)).perform(replaceText("NEW DESCRIPTION"))
        onView(withId(R.id.save_task_fab)).perform(click())

        // Verify task is displayed on screen in the task list.
        onView(withText("NEW TITLE")).check(matches(isDisplayed()))
        // Verify previous task is not displayed.
        onView(withText("TITLE1")).check(doesNotExist())


        // Make sure the activity is closed before resetting the db:
        activityScenario.close()
    }
}

