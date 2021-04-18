package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TasksViewModelTest{

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @Test
    fun addNewTask_setsNewTaskEvent() {

        // Given a fresh TasksViewModel
        val tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())


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
        // Given a fresh ViewModel
        val taskViewModel_ =TasksViewModel(ApplicationProvider.getApplicationContext())
//You call the setFiltering method, passing in the ALL_TASKS filter type enum
        // When the filter type is ALL_TASKS
        taskViewModel_.setFiltering(TasksFilterType.ALL_TASKS)
//You check that the tasksAddViewVisible is true, using the getOrAwaitNextValue method
        // Then the "Add task" action is visible
        assertThat(taskViewModel_.tasksAddViewVisible.getOrAwaitValue(), `is`(true))

    }


}