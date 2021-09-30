package com.androidapp.todo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.androidapp.todo.TaskAdapter
import com.androidapp.todo.database.TaskDatabase
import com.androidapp.todo.databinding.ActivityListBinding
import com.androidapp.todo.entities.Task

class List : AppCompatActivity() {

    lateinit var db : TaskDatabase
    private lateinit var bindingList: ActivityListBinding

    lateinit var itens : ArrayList<Task>
    lateinit var title : TaskDatabase
    lateinit var subtitle : TaskDatabase
    lateinit var text : TaskDatabase
    lateinit var date : TaskDatabase

    lateinit var register : TaskDatabase
    lateinit var cancel : TaskDatabase
    lateinit var task : Task


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingList = ActivityListBinding.inflate(layoutInflater)
        val view = bindingList.root
        setContentView(view)

        db = Room.databaseBuilder(applicationContext, TaskDatabase::class.java, "TaskList").build()

        consult()
    }

    fun consult(){
        Thread{
            itens = db.TaskDao().getAllTasks() as ArrayList<Task>
            fill()
            }.start()
    }

    fun fill(){
        runOnUiThread{
            val adapter = TaskAdapter(applicationContext, itens)
            bindingList.recycler.layoutManager = LinearLayoutManager(applicationContext)
            bindingList.recycler.itemAnimator = DefaultItemAnimator()
            bindingList.recycler.adapter = adapter
        }
    }
}