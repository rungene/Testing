package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TasksViewModelTest{

    // Subject under test
    private lateinit var tasksViewModel: TasksViewModel
    //Create a method called setupViewModel
    //Annotate it with @Before
    //Move the view model instantiation code to setupViewModel:
    //NB Do not do the following, do not initialize the tasksViewModel with its definition:
    //This will cause the same instance to be used for all tests. This is something you should
    // avoid because each test should have a fresh instance of the subject under test
    // (the ViewModel in this case).
    @Before
    fun setupViewModel() {
        tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())
    }
//   // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @Test
    fun addNewTask_setsNewTaskEvent() {


        // When adding a new task
        tasksViewModel.addNewTask()

        // Then the new task event is triggered
        //  test LiveData
      val value =  tasksViewModel.newTaskEvent.getOrAwaitValue()
        //Assert that the value is not null:
        assertThat(value.getContentIfNotHandled(), (not(nullValue())))

    }
    @Test
    fun setFilterAllTasks_tasksAddViewVisible() {
//You create your tasksViewModel using the same AndroidX ApplicationProvider.getApplicationContext() statement

//You call the setFiltering method, passing in the ALL_TASKS filter type enum
        // When the filter type is ALL_TASKS
        tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)
//You check that the tasksAddViewVisible is true, using the getOrAwaitNextValue method
        // Then the "Add task" action is visible
        assertThat(tasksViewModel.tasksAddViewVisible.getOrAwaitValue(), `is`(true))

    }


}