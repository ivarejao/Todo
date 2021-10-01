package com.androidapp.todo.database

import androidx.room.Database
import androidx.room.Entity
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.androidapp.todo.dao.TaskDao
import com.androidapp.todo.entities.Task
import com.androidapp.todo.entities.DateConverter

@Database(entities = [Task::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class TaskDatabase : RoomDatabase(){

    abstract fun TaskDao(): TaskDao

}