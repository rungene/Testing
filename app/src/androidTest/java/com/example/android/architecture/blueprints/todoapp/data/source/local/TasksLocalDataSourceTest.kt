package com.example.android.architecture.blueprints.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource
import com.example.android.architecture.blueprints.todoapp.data.Result.Success
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.succeeded
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test


/*the only real difference between this and the DAO testing code is that the TasksLocalDataSource
can be considered a medium "integration" test (as seen by the @MediumTest annotation), because
the TasksLocalDataSourceTest will test both the code in TasksLocalDataSource and how it integrates
with the DAO code.*/
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class TasksLocalDataSourceTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // components under test
    private lateinit var localDataSource: TasksLocalDataSource
    private lateinit var database: ToDoDatabase

    //Make a @Before method for initializing your database and datasource.
    @Before
    fun setup() {
        // Using an in-memory database for testing, because it doesn't survive killing the process.
        database = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                ToDoDatabase::class.java
        )
     //Normally Room doesn't allow database queries to be run on the main thread.
                // Calling allowMainThreadQueries turns off this check. Don't do this in
                // production code!
                .allowMainThreadQueries()
                .build()
//Instantiate the TasksLocalDataSource, using your database and Dispatchers.Main.
// This will run your queries on the main thread (this is allowed because of allowMainThreadQueries
        localDataSource =
                TasksLocalDataSource(
                        database.taskDao(),
                        Dispatchers.Main
                )
    }
//clean up your database using database.close().
    @After
    fun cleanUp() {
        database.close()
    }
    // runBlocking is used here because of https://github.com/Kotlin/kotlinx.coroutines/issues/1204
// TODO: Replace with runBlockingTest once issue is resolved
    @Test
    fun saveTask_retrievesTask() = runBlocking {
        // GIVEN - A new task saved in the database.
        //Creates a task and inserts it into the database.
        val newTask = Task("title", "description", false)
        localDataSource.saveTask(newTask)

        // WHEN  - Task retrieved by ID.
       // Retrieves the task using its id.
        val result = localDataSource.getTask(newTask.id)

        // THEN - Same task is returned.
        //Asserts that that task was retrieved, and that all its properties match the inserted task.
        assertThat(result.succeeded, `is`(true))
        result as Success
        assertThat(result.data.title, `is`("title"))
        assertThat(result.data.description, `is`("description"))
        assertThat(result.data.isCompleted, `is`(false))
    }
    //The only real difference from the analogous DAO test is that the local data source
// returns an instance of the sealed Result class, which is the format the repository expects.

    @Test
    fun completeTask_retrievedTaskIsComplete()= runBlocking{
        // 1. Save a new active task in the local data source.
        // Given a new task in the persistent repository
        val newTask = Task("title")
        localDataSource.saveTask(newTask)

        // 2. Mark it as complete.
        // When completed in the persistent repository
        localDataSource.completeTask(newTask)
        val result = localDataSource.getTask(newTask.id)

        // 3. Check that the task can be retrieved from the local data source and is complete.
        // Then the task can be retrieved from the persistent repository and is complete
        assertThat(result.succeeded, `is`(true))
        result as Success
        assertThat(result.data.title, `is`(newTask.title))
        assertThat(result.data.isCompleted, `is`(true))


    }

}