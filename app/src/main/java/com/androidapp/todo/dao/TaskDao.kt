package com.androidapp.todo.dao

import androidx.room.*

import com.androidapp.todo.entities.Task

@Dao
interface TaskDao {

    @Query("select * from task")
    fun getAllTasks(): List<Task>

    @Query("select * from task where title like :title")
    fun getTaskByName(title: String): Task

    @Insert
    fun insertTasks(vararg tasks: Task)

    @Delete
    fun deleteTask(task: Task)

    @Update
    fun updateTask(task: Task)

}