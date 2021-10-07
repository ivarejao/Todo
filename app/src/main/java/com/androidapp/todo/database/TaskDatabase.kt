package com.androidapp.todo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.androidapp.todo.dao.TaskDao
import com.androidapp.todo.entities.Task
import com.androidapp.todo.entities.DateConverter

/**
 * Classe necessária para se implementar um banco de dados usando o RoomDatabase
 */
@Database(entities = [Task::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class TaskDatabase : RoomDatabase(){

    abstract fun TaskDao(): TaskDao

}