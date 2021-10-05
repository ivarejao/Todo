package com.androidapp.todo.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.style.UnderlineSpan
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.androidapp.todo.database.TaskDatabase
import com.androidapp.todo.databinding.ActivityMainBinding
import com.androidapp.todo.entities.Task
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    // Banco de dados da aplicação.
    private lateinit var db : TaskDatabase
    private lateinit var binding: ActivityMainBinding

    // Calendario e data como variáveis globais.
    private var cal:Calendar = Calendar.getInstance()
    private var date : String = ""

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        // Inicializa a tela principal da aplicação.
        super.onCreate(savedInstanceState)

        // Fazendo binding da tela para não ter que buscar os elementos da mesma toda hora.
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        if (intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT).toString().isNotEmpty()){
            binding.text.setText(intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT).toString());
        }

        setContentView(view)

        // Recupera o banco de dados.
        db = Room.databaseBuilder(applicationContext, TaskDatabase::class.java, "TaskList").build()

        // Evento que ocorre quando a data for selecionada dentro do dialog.
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                // Faz a formatação da data lida usando o locale da Itália pois era o que mais se adaptava
                // ao nosso sistema de data dentre os locales disponiveis.
                val formatador = SimpleDateFormat("dd/MM/yyyy", Locale.ITALY)

                date = formatador.format(cal.time)
            }

        setListeners(binding)

        // Criando o handler para quando o botão 'DATA' for clicado.
        binding.date.setOnClickListener{
            // Cria um dialog para a seleção da data.
            DatePickerDialog(this@MainActivity, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Criando o handler para quando o botão 'CADASTRAR' for clicado.
        binding.register.setOnClickListener{

            // Recuperação dos valores digitados nos campos.
            val title = binding.title.text.toString()
            val subtitle = binding.subtitle.text.toString()
            var text = binding.text.text.toString()
            val sync = binding.checkBox.isChecked

            // Tratamento das variáveis lidas.
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
                date.isEmpty() -> {
                    Toast.makeText(this, "Selecione uma data para a tarefa.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                else -> {
                    // Caso as variáveis estejam todas preenchidas, executa este código.
                    val format = SimpleDateFormat("dd/MM/yyyy")

                    // Cria uma instancia da classe tarefa.
                    val task = Task(0, title, subtitle, text, format.parse(date), sync)

                    // Insere o objeto no banco de dados e limpa a tela.
                    // Dentro de uma thread para não atrapalhar o usuário.
                    Thread{
                        db.TaskDao().insertTasks(task)
                        runOnUiThread {
                            binding.title.text = createEditable()
                            binding.subtitle.text = createEditable()
                            binding.text.text = createEditable()
                            date = ""
                            if (sync)
                                binding.checkBox.toggle()
                        }
                    }.start()

                    // Muda a tela para a activity List
                    val intent = Intent(applicationContext, kotlin.collections.List::class.java)
                    startActivity(intent)
                }
            }
        }
        if (binding.checkBox.isChecked == true){

        }

        // Criando o handler para quando o botão 'LISTAR' for clicado.
        binding.back.setOnClickListener{
            // Muda a tela para a activity List
            val intent = Intent(applicationContext, List::class.java)
            startActivity(intent)
        }
    }

    private fun createEditable() = Editable.Factory.getInstance().newEditable("")

    fun setListeners(binding: ActivityMainBinding){

        binding.boldbtn.setOnClickListener {
        //            Toast.makeText(this, "Actually on this", Toast.LENGTH_SHORT).show()
            var editxt = binding.text as EditText
            val init = editxt.selectionStart
            val end = editxt.selectionEnd
            editxt.text.setSpan(
                android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                init, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

        }

        binding.italicbtn.setOnClickListener {
            var editxt = binding.text as EditText
            val init = editxt.selectionStart
            val end = editxt.selectionEnd
            editxt.text.setSpan(
                android.text.style.StyleSpan(android.graphics.Typeface.ITALIC),
                init, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

        }


        binding.underlinebtn.setOnClickListener {
            var editxt = binding.text as EditText
            val init = editxt.selectionStart
            val end = editxt.selectionEnd
            editxt.text.setSpan(
                UnderlineSpan(),
                init, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

        }
    }
}