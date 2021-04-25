package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.Event
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeDataSource
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
class TasksViewModelTest{

   // Replace Dispatcher.Main with TestCoroutineDispatcher
   val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
    //called before every test.
    @Before
    fun setupDispatcher() {
        Dispatchers.setMain(testDispatcher)
    }
//method that cleans everything up after running each test
    @After
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }


    private lateinit var tasksRepository: FakeTestRepository

    // Subject under test
    private lateinit var tasksViewModel: TasksViewModel

    //   // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    //Create a method called setupViewModel
    //Annotate it with @Before
    //Move the view model instantiation code to setupViewModel:
    //NB Do not do the following, do not initialize the tasksViewModel with its definition:
    //This will cause the same instance to be used for all tests. This is something you should
    // avoid because each test should have a fresh instance of the subject under test
    // (the ViewModel in this case).
    @Before
    fun setupViewModel() {
        //we initialize the tasks to 3, with one active and two completed
        tasksRepository = FakeTestRepository()
        val task1 = Task("Title1","Description1")
        val task2 = Task("Title2","Description2",true)
        val task3 = Task("Title3","Description3",true)
        tasksRepository.addTasks(task1,task2,task3)

        tasksViewModel =TasksViewModel(tasksRepository)


    }



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

/*    write a test for code that uses viewModelScope - you'll write a test that checks that when a
    task is completed, the snackbar shows the correct message. To complete this test, you'll need
    to swap the default Dispatchers.Main for a TestCoroutineDispatcher.*/
    @Test
    fun completeTask_dataAndSnackbarUpdated() {
        // Create an active task and add it to the repository.
        val task = Task("Title", "Description")
        tasksRepository.addTasks(task)

        // Mark the task as complete task.
        tasksViewModel.completeTask(task, true)

        // Verify the task is completed.
        assertThat(tasksRepository.tasksServiceData[task.id]?.isCompleted, `is`(true))

        // Assert that the snackbar has been updated with the correct text.
        val snackbarText: Event<Int> =  tasksViewModel.snackbarText.getOrAwaitValue()
        assertThat(snackbarText.getContentIfNotHandled(), `is`(R.string.task_marked_complete))
    }

}