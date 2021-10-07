package com.androidapp.todo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.androidapp.todo.R
import com.androidapp.todo.viewHolder.TaskViewHolder
import com.androidapp.todo.entities.Task
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Classe para adaptar as tarefas na lista.
 */
class TaskAdapter(var context: Context, var itens: ArrayList<Task>) : RecyclerView.Adapter<TaskViewHolder>() {

    lateinit var task: Task
    var selectedPosition: Int = 0

    /**
     * Cria uma view a partir da ViewGroup passada e retorna um viewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)

        return TaskViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SimpleDateFormat")
    /**
     * Faz o bind de cada elemento da lista ao viewHolder, para indicar como eles serão exibidos e habilitar os handlers.
     *
     * @param viewHolder o viewHolder da lista.
     * @param position a posição do item no itemView.
     */
    override fun onBindViewHolder(viewHolder: TaskViewHolder, position: Int) {
        // Ordena as tarefas
        itens.sortWith { t1: Task, t2: Task ->
            if (t1.date!! > t2.date) 1 else if (t1.date!! < t2.date) -1 else 0
        }

        val format  = SimpleDateFormat("dd/MM/yyyy")

        // Definindo como vai aparecer cada item do recyclerView
        task = itens[viewHolder.bindingAdapterPosition]
        (task.title).also { viewHolder.title.text = it }
        (task.subtitle).also { viewHolder.subtitle.text = it }
        (format.format(task.date!!)).also { viewHolder.date.text = it }

        val dayTask = task.date!!
        val dayCurrent = Calendar.getInstance(Locale.ITALY).time

        var diff = dayTask.time - dayCurrent.time

        val time = TimeUnit.DAYS
        diff = time.convert(diff, TimeUnit.MILLISECONDS)

        val isLate = diff < 0
        if (isLate) diff *=-1

        val str =  when {
                dayTask == dayCurrent && !isLate -> "Today"
                dayTask != dayCurrent && diff >= 0.toLong() && diff < 1.toLong() && !isLate -> "Less than\n one day"
                diff == 1.toLong() && diff < 2.toLong() -> "$diff day\n" + if (!isLate) "left" else "late"
                else                                    -> "$diff days\n"+ if (!isLate) "left" else "late"
            }
        (str).also{viewHolder.timeRemaining.text = it}

        val color = when{
            isLate                  -> "#FF4842"
            diff in 0..4            -> "#ff8e42"
            diff in 5..9            -> "#ffc642"
            diff in 10..29          -> "#7bff42"
            else                    -> "#4248ff"
        }

        viewHolder.colorBg.setBackgroundColor(Color.parseColor(color))

        // Criando o handler para quando o item for clicado.
        viewHolder.itemView.setOnClickListener{
            task = itens[viewHolder.bindingAdapterPosition]
            selectedPosition = viewHolder.bindingAdapterPosition
            Toast.makeText(context, "Segure para abrir o menu de opções!", Toast.LENGTH_LONG).show()
        }

        // Menu de ação que será exibido quando houver um LonClick
        viewHolder.itemView.setOnCreateContextMenuListener { menu, _, _ ->
            menu!!.setHeaderTitle("Ações: ")
            menu.add(0, 0, 0, "Visualizar")
            menu.add(0, 1, 1, "Editar")
            menu.add(0, 2, 2, "Deletar")

            task = itens[viewHolder.bindingAdapterPosition]
            selectedPosition = viewHolder.bindingAdapterPosition
        }
    }

    /**
     * Obtem o tamanho da lista.
     *
     * @return tamanho da lista
     */
    override fun getItemCount(): Int {
        return itens.size
    }

    /**
     * Faz a exclusão de uma tarefa da lista, apenas visualmente.
     */
    fun deleteTask(){
        itens.remove(task)
        this.notifyItemRemoved(selectedPosition)
    }
}