package com.androidapp.todo.activities

import android.app.DatePickerDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.androidapp.todo.R
import com.androidapp.todo.TaskAdapter
import com.androidapp.todo.database.TaskDatabase
import com.androidapp.todo.databinding.ActivityListBinding
import com.androidapp.todo.entities.Task
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class List : AppCompatActivity() {

    lateinit var db : TaskDatabase
    private lateinit var bindingList: ActivityListBinding

    lateinit var itens : ArrayList<Task>
    lateinit var title : EditText
    lateinit var subtitle : EditText
    lateinit var text : EditText
    lateinit var dateButton : Button

    lateinit var edit : Button
    lateinit var cancel : Button
    lateinit var task : Task

    var cal:Calendar = Calendar.getInstance()

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

        when (item!!.itemId) {
            0 -> {
                displayDialog()
            }
            1 -> {
                displayEditDialog()
            }
            2 -> {
                adapter.deleteTask()
                deleteTask(task)
            }
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

    private fun displayEditDialog(){
        // Cria o dialog a ser exibido
        val dialog = Dialog(this)
        dialog.setTitle("Editar")
        dialog.setContentView(R.layout.edit)
        dialog.setCancelable(true)

        // Recupera os elementos do dialog
        title = dialog.findViewById(R.id.title) as EditText
        subtitle = dialog.findViewById(R.id.subtitle) as EditText
        text = dialog.findViewById(R.id.text) as EditText
        dateButton = dialog.findViewById(R.id.date) as Button

        edit = dialog.findViewById(R.id.edit) as Button
        cancel = dialog.findViewById(R.id.cancel) as Button

        val formatador = SimpleDateFormat("dd//MM/yyyy", Locale.ITALY)
        var date : String = ""

        // Cria o evento de seleção da data.
        val dateSetListener = object : DatePickerDialog.OnDateSetListener{
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                date = formatador.format(cal.time)
            }
        }

        dateButton.setOnClickListener{
            DatePickerDialog(this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Preenchendo a tela com o que tinha selecionado
        title.setText(task.title)
        subtitle.setText(task.subtitle)
        text.setText(task.text)
        date = formatador.format(task.date!!)

        //
        edit.setOnClickListener(View.OnClickListener {
            when {
                title.text.toString().trim().isEmpty() -> {
                    Toast.makeText(this, "Título da tarefa não pode estar em branco!", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }
                subtitle.text.toString().trim().isEmpty() -> {
                    Toast.makeText(this, "Subtitulo da tarefa não pode estar em branco!", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }
                text.text.toString().trim().isEmpty() -> {
                    Toast.makeText(this, "Texto da tarefa não pode estar em branco!", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }
                date.isEmpty() -> {
                    Toast.makeText(this, "Selecione uma data para a tarefa.", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }
                else -> {
                    task.title = title.text.toString()
                    task.subtitle = subtitle.text.toString()
                    task.text = text.text.toString()
                    task.date = formatador.parse(date)
                    task.sync = false

                    updateTask(task)
                    dialog.dismiss()
                }
            }
        })

        cancel.setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun displayDialog() {
        // Cria o dialog a ser exibido
        val dialog = Dialog(this)
        dialog.setTitle("Editar")
        dialog.setContentView(R.layout.task)
        dialog.setCancelable(true)

        // Recupera os elementos do dialog
        val vTitle: TextView = dialog.findViewById(R.id.title) as TextView
        val vSubtitle: TextView = dialog.findViewById(R.id.subtitle) as TextView
        val vText: TextView = dialog.findViewById(R.id.text) as TextView
        val vDate: TextView = dialog.findViewById(R.id.date) as TextView

        val formatador = SimpleDateFormat("dd//MM/yyyy", Locale.ITALY)

        ("Título: " + task.title).also { vTitle.text = it }
        ("Subtítulo: " + task.subtitle).also { vSubtitle.text = it }
        ("Texto: " + task.text).also { vText.text = it }
        ("Data: " + formatador.format(task.date!!)).also { vDate.text = it }

        dialog.show()
    }

    private fun updateTask(task: Task){
        Thread{
            db.TaskDao().updateTask(task)
            consult()
        }.start()
    }
}