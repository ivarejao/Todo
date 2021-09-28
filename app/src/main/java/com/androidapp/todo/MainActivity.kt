package com.androidapp.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.androidapp.todo.database.TaskDatabase
import com.androidapp.todo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var db : TaskDatabase
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        db = Room.databaseBuilder(applicationContext, TaskDatabase::class.java, "TaskList").build()
    }
}