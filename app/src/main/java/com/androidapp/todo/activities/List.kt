package com.androidapp.todo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.androidapp.todo.TaskAdapter
import com.androidapp.todo.database.TaskDatabase
import com.androidapp.todo.databinding.ActivityListBinding
import com.androidapp.todo.entities.Task
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.collections.ArrayList

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

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val adapter = bindingList.recycler.adapter as TaskAdapter
        task = adapter.task

        if (item!!.itemId == 0){
            Toast.makeText(applicationContext, "Menu editar", Toast.LENGTH_LONG).show()
        }
        else if (item!!.itemId == 1){
            adapter.deleteTask()
            deleteTask(task)
        }

        return super.onContextItemSelected(item)
    }

    // Exclusão do banco de dados.
    private fun deleteTask(task: Task){
        val timer = Timer()

        // Se o usuário clicar em desfazer o timer é cancelado e é feita outra consulta ao banco de dados para popular a tela.
        val snackbar = Snackbar.make(bindingList.recycler, "Tarefa '" + task.title + "' excluida com sucesso", Snackbar.LENGTH_LONG)
        snackbar.setAction("Desfazer", View.OnClickListener {
            timer.cancel()
            consult()
        }).show()

        // Caso o timer não seja cancelado, ocorre a exclusão da tarefa no banco de dados.
        timer.schedule(object: TimerTask(){
            override fun run(){
                db.TaskDao().deleteTask(task)
            }
        }, 5000)
    }
}