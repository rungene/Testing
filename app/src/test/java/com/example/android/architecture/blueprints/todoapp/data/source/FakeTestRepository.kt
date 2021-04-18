package com.example.android.architecture.blueprints.todoapp.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.runBlocking

class FakeTestRepository :TasksRepository   {
//Add both a LinkedHashMap variable representing the current list of tasks and a MutableLiveData
// for your observable tasks:
var tasksServiceData: LinkedHashMap<String, Task> = LinkedHashMap()

    private val observableTasks = MutableLiveData<Result<List<Task>>>()

    //This method should just take the tasksServiceData and turn it into a list using tasksServiceData.
// values.toList() and then return that as a Success result.
    override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>> {
  return Result.Success(tasksServiceData.values.toList())
    }

    override suspend fun refreshTasks() {
        TODO("Not yet implemented")
    }
    //observeTasks - Create a coroutine using runBlocking and run refreshTasks, then return
// observableTasks.
    override fun observeTasks(): LiveData<Result<List<Task>>> {
   runBlocking { refreshTasks() }
        return observableTasks
    }
//Updates the value of observableTasks to be what is returned by getTasks().
    override suspend fun refreshTask(taskId: String) {
     observableTasks.value = getTasks()
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTask(taskId: String, forceUpdate: Boolean): Result<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun saveTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun completeTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun completeTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun activateTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun activateTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun clearCompletedTasks() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllTasks() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(taskId: String) {
        TODO("Not yet implemented")
    }
//Add the addTasks method, which takes in a vararg of tasks, adds each to the HashMap
// and then refreshes the tasks:
    fun addTasks(vararg tasks: Task) {
        for (task in tasks) {
            tasksServiceData[task.id] = task
        }
        runBlocking { refreshTasks() }
    }
}