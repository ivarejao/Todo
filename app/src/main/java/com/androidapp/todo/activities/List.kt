package com.androidapp.todo.activities

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.style.UnderlineSpan
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.androidapp.todo.R
import com.androidapp.todo.adapter.TaskAdapter
import com.androidapp.todo.database.TaskDatabase
import com.androidapp.todo.databinding.ActivityListBinding
import com.androidapp.todo.entities.Task
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

/**
 * Classe que manipula a activity de listar as tarefas.
 */
class List : AppCompatActivity() {

    /**
     * Banco de dados.
     */
    lateinit var db : TaskDatabase
    private lateinit var bindingList: ActivityListBinding

    private lateinit var itens : ArrayList<Task>

    /**
     * Titulo da tarefa.
     */
    lateinit var title : EditText

    /**
     * Subtitulo da tarefa.
     */
    lateinit var subtitle : EditText

    /**
     * Texto da tarefa.
     */
    lateinit var text : EditText

    // Variáveis para fazer referência aos elementos da tela
    private lateinit var dateButton : Button
    private lateinit var boldbtn : Button
    private lateinit var italicbtn : Button
    private lateinit var underlinebtn : Button

    private lateinit var register : Button
    private lateinit var back : Button
    private lateinit var task : Task

    // Calendario como variável global.
    private var cal:Calendar = Calendar.getInstance()

    /**
     * Faz a criação da tela, instancia o banco de dados e chama a função consult().
     *
     * @param savedInstanceState Parametro opcional que representa mensagens vindas de outras activities.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Fazendo binding da tela para não ter que buscar os elementos da mesma toda hora.
        bindingList = ActivityListBinding.inflate(layoutInflater)
        val view = bindingList.root
        setContentView(view)

        // Recupera o banco de dados.
        db = Room.databaseBuilder(applicationContext, TaskDatabase::class.java, "TaskList").build()
        consult()
        val createTask = bindingList.createbtn
        createTask.setOnClickListener {
            // Muda a tela para a activity main
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Obtem as tarefas do banco de dados e as armazena na variável da classe 'itens'.
     */
    fun consult(){
        Thread{
            itens = db.TaskDao().getAllTasks() as ArrayList<Task>
            fill()
            }.start()
    }

    /**
     * Preenche a tela com as tarefas contidas na variável da classe 'itens'.
     */
    fun fill(){
        runOnUiThread{
            val adapter = TaskAdapter(applicationContext, itens)

            bindingList.recycler.layoutManager = LinearLayoutManager(applicationContext)
            bindingList.recycler.itemAnimator = DefaultItemAnimator()
            bindingList.recycler.adapter = adapter
        }
    }

    /**
     * Interpreta o clique do usuário e invoca a função equivalente (visualizar, editar, excluir).
     *
     * @param item É o elemento da lista ao qual o menu que será aberto se refere.
     * @return Boolean indicando se alguma posição do menu foi clicada ou não.
     */
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val adapter = bindingList.recycler.adapter as TaskAdapter
        task = adapter.task

        // Caso 0 -> Visualizar
        // Caso 1 -> Editar
        // Caso 2 -> Deletar
        when (item.itemId) {
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

    /**
     * Faz o display um botao de desfazer por aproximadamente 5 segundos.
     * Caso o botão seja apertado, invoca consult().
     * Caso o timer acabe, deleta a tarefa do banco de dados.
     *
     * @param task Tarefa a ser excluida.
     */
    fun deleteTask(task: Task){
        val timer = Timer()

        // Se o usuário clicar em desfazer o timer é cancelado e é feita outra consulta ao banco de dados para popular a tela.
        val snackbar = Snackbar.make(bindingList.recycler, "Tarefa '" + task.title + "' excluida com sucesso", Snackbar.LENGTH_LONG)
        snackbar.setAction("Desfazer") {
            timer.cancel()
            consult()
        }.show()

        // Caso o timer não seja cancelado, ocorre a exclusão da tarefa no banco de dados.
        timer.schedule(object: TimerTask(){
            override fun run(){
                db.TaskDao().deleteTask(task)
            }
        }, 5000)
    }

    /**
     * Cria o dialogo de edição de tarefa.
     */
    private fun displayEditDialog(){
        val dialog = Dialog(this)
        dialog.setTitle("Editar")
        dialog.setContentView(R.layout.activity_main)
        dialog.setCancelable(true)
        val lp : WindowManager.LayoutParams = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT

        // Recupera os elementos do dialog
        title = dialog.findViewById(R.id.title) as EditText
        subtitle = dialog.findViewById(R.id.subtitle) as EditText
        text = dialog.findViewById(R.id.text) as EditText
        dateButton = dialog.findViewById(R.id.date) as Button
        boldbtn = dialog.findViewById(R.id.boldbtn) as Button
        italicbtn = dialog.findViewById(R.id.italicbtn) as Button
        underlinebtn = dialog.findViewById(R.id.underlinebtn) as Button

        register = dialog.findViewById(R.id.register) as Button
        back = dialog.findViewById(R.id.back) as Button

        val formatador = SimpleDateFormat("dd/MM/yyyy", Locale.ITALY)

        // Preenchendo a tela com o que tinha no banco de dados para aquela tarefa
        title.setText(task.title)
        subtitle.setText(task.subtitle)
        text.setText(task.text)
        var date = formatador.format(task.date!!)

        // Cria o evento de seleção da data.
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                date = formatador.format(cal.time)
            }

        boldbtn.setOnClickListener {
            val editxt = text
            val init = editxt.selectionStart
            val end = editxt.selectionEnd
            editxt.text.setSpan(
                android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                init, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        italicbtn.setOnClickListener {
            val editxt = text
            val init = editxt.selectionStart
            val end = editxt.selectionEnd
            editxt.text.setSpan(
                android.text.style.StyleSpan(android.graphics.Typeface.ITALIC),
                init, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        underlinebtn.setOnClickListener {
            val editxt = text
            val init = editxt.selectionStart
            val end = editxt.selectionEnd
            editxt.text.setSpan(
                UnderlineSpan(),
                init, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        // Criando o handler para quando o botão 'DATA' for clicado.
        dateButton.setOnClickListener{
            DatePickerDialog(this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Criando o handler para quando o botão 'EDITAR' for clicado.
        register.setOnClickListener(View.OnClickListener {
            // Tratamento das variáveis lidas.
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
                    // Caso as variáveis estejam todas preenchidas, executa este código.
                    // Atribui os valores lidos à tarefa.
                    task.title = title.text.toString()
                    task.subtitle = subtitle.text.toString()
                    task.text = text.text.toString()
                    task.date = formatador.parse(date)
                    task.sync = false

                    // E atualiza a tarefa no banco de dados.
                    updateTask(task)
                    dialog.dismiss()
                }
            }
        })

        // Set dialog background as transparent
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Criando o handler para quando o botão 'CANCELAR' for clicado.
        back.setOnClickListener{
            // Simplesmente fecha o dialog.
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.attributes = lp
    }

    /**
     * Cria um dialogo para mostrar a tarefa que foi selecionada.
     */
    fun displayDialog() {
        val dialog = Dialog(this)
        dialog.setTitle("Editar")
        dialog.setContentView(R.layout.task)
        dialog.setCancelable(true)

        // Recupera os elementos do dialog.
        // Variáveis criadas localmente e não usando as que existem na classe pois seus tipos na tela são diferentes.
        // Essas variáveis não são possiveis de serem editadas pelo usuário.
        val vTitle: TextView = dialog.findViewById(R.id.title) as TextView
        val vSubtitle: TextView = dialog.findViewById(R.id.subtitle) as TextView
        val vText: TextView = dialog.findViewById(R.id.text) as TextView
        val vDate: TextView = dialog.findViewById(R.id.date) as TextView

        val formatador = SimpleDateFormat("dd/MM/yyyy", Locale.ITALY)

        // Formatando os textos para mostrar ao usuário.
        (task.title).also { vTitle.text = it }
        (task.subtitle).also { vSubtitle.text = it }
        (task.text).also { vText.text = it }
        ("Data: " + formatador.format(task.date!!)).also { vDate.text = it }

        // Set dialog background as transparent
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Set back button
        back = dialog.findViewById(R.id.back) as Button
        back.setOnClickListener{
            // Simplesmente fecha o dialog.
            dialog.dismiss()
        }

        dialog.show()
    }

    /**
     * Atualiza uma dada tarefa no banco de dados e invoca consult().
     *
     * @param task Tarefa a ser atualizada.
     */
    fun updateTask(task: Task){
        Thread{
            db.TaskDao().updateTask(task)
            consult()
        }.start()
    }
}