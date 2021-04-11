package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Test

class StatisticsUtilsTest{
    //if there is no completed task and one active task,
    //then there are 100 % active tasks and 0% completed tasks 


    @Test
    fun getActiveAndCompletedStats_noCompleted_returnsHundredZero() {

//GIVEN a list of tasks with a single,active, task
        // Create an active tasks (the false makes this active)
        val tasks = listOf<Task>(
                Task("title", "desc", isCompleted = false)
        )

        //WHEN you call getActiveAndCompletedStats
        // Call our function.When the list of tasks is computed with a completed task
        val result = getActiveAndCompletedStats(tasks)

//THEN there are 0% completed tasks and 100% active tasks
        // Check the result.Then the percentages are 0 and 100

       assertThat(result.completedTasksPercent,`is`(0f))
        assertThat(result.activeTasksPercent,`is`(100f))

    }

    //if there's 2 completed tasks and 3 active tasks
    //then there are 40% completed tasks and 60% active tasks

    @Test
    fun getActiveAndCompletedStats_both_returnsFortySixty() {
        // Given 3 completed tasks and 2 active tasks
        val tasks = listOf<Task>(
                Task("title", "desc", isCompleted = true),
                Task("title", "desc", isCompleted = true),
                Task("title", "desc", isCompleted = false),
                Task("title", "desc", isCompleted = false),
                Task("title", "desc", isCompleted = false)


        )
        // When the list of tasks is computed
        val result = getActiveAndCompletedStats(tasks)

        // Check the result- Then the result is 40-60
        assertEquals(40f, result.completedTasksPercent)
        assertEquals(60f, result.activeTasksPercent)
    }

    @Test
    fun getActiveAndCompletedStats_empty_returnsZeros() {

        val tasks = emptyList<Task>()
        // When there are no tasks
        val result = getActiveAndCompletedStats(tasks)

        // Check the result
        assertEquals(0f,result.completedTasksPercent)
        assertEquals(0f, result.activeTasksPercent)
    }

    @Test
    fun getActiveAndCompletedStats_error_returnsZeros() {

        val tasks = null
        // When there's an error loading stats
        val result = getActiveAndCompletedStats(tasks)

        // Both active and completed tasks are 0
        assertEquals(0f,result.completedTasksPercent)
        assertEquals(0f, result.activeTasksPercent)
    }

}