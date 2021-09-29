package com.androidapp.todo

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.DatePicker
import android.widget.Toast
import androidx.room.Room
import com.androidapp.todo.database.TaskDatabase
import com.androidapp.todo.databinding.ActivityMainBinding
import com.androidapp.todo.entities.Task
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var db : TaskDatabase
    private lateinit var binding: ActivityMainBinding
    private var date : String? = null

    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Inicializa a tela principal da aplicação.
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Cria o banco de dados.
        db = Room.databaseBuilder(applicationContext, TaskDatabase::class.java, "TaskList").build()

        // Cria o evento de seleção da data.
        var dateSetListener = object : DatePickerDialog.OnDateSetListener{
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val formatador = SimpleDateFormat("dd//MM/yyyy", Locale.ITALY)

                date = formatador.format(cal.time)
            }

        }

        binding.date.setOnClickListener{
            DatePickerDialog(this@MainActivity, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.register.setOnClickListener{

            val title = binding.title.text.toString()
            val subtitle = binding.subtitle.text.toString()
            val text = binding.text.text.toString()
            val sync = binding.checkBox.isChecked

            when {
                title.trim().isEmpty() -> {
                    Toast.makeText(this, "Título da tarefa não pode estar em branco!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                subtitle.trim().isEmpty() -> {
                    Toast.makeText(this, "Subtitulo da tarefa não pode estar em branco!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                text.trim().isEmpty() -> {
                    Toast.makeText(this, "Texto da tarefa não pode estar em branco!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                date.isNullOrEmpty() -> {
                    Toast.makeText(this, "Selecione uma data para a tarefa.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                else -> {
                    val format = SimpleDateFormat("dd//MM/yyyy")

                    val task = Task(
                        0,
                        title,
                        subtitle,
                        text,
                        format.parse(date.toString()),
                        sync
                    )

                    Thread(Runnable {
                        db.TaskDao().insertTasks(task)
                        runOnUiThread(Runnable {
                            val str:String = ""
                            binding.title.text = createEditable(str)
                            binding.subtitle.text = createEditable(str)
                            binding.text.text = createEditable(str)
                            date = null
                            if(sync)
                                binding.checkBox.toggle()
                            }
                        )
                    }).start()
                }
            }
        }
    }

    private fun createEditable(text : String) = Editable.Factory.getInstance().newEditable(text)

}