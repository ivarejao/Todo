package com.androidapp.todo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.androidapp.todo.entities.Task
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.Comparator


class TaskAdapter(var context: Context, var itens: ArrayList<Task>) : RecyclerView.Adapter<TaskViewHolder>() {

    lateinit var task: Task
    var selectedPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)

        return TaskViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(viewHolder: TaskViewHolder, position: Int) {
        // Ordena as tarefas
        Collections.sort(itens, Comparator{ t1:Task, t2:Task ->
                if (t1.date!! > t2.date) 1 else if (t1.date!! < t2.date) -1 else 0;
            }
        )

        val format  = SimpleDateFormat("dd/MM/yyyy")

        // Definindo como vai aparecer cada item do recyclerView
        task = itens[viewHolder.bindingAdapterPosition]
        (task.title).also { viewHolder.title.text = it }
        (task.subtitle).also { viewHolder.subtitle.text = it }
        (format.format(task.date!!)).also { viewHolder.date.text = it }

        var taskD = task.date!!.time;
        var currentDay = Calendar.getInstance(Locale.ITALY).time.time
        var diff = taskD - currentDay

        var time = TimeUnit.DAYS
        diff = time.convert(diff, TimeUnit.MILLISECONDS)

        val isLate = diff < 0
        if (isLate) diff *=-1

        var str =  when {
                diff > 0.toLong() && diff < 1.toLong() && !isLate -> "Today"
                diff == 1.toLong() && diff < 2.toLong() -> "$diff day\n" + if (isLate) "left" else "late"
                else                                    -> "$diff days\n"+ if (isLate) "left" else "late"
            }
        (str).also{viewHolder.timeRemaining.text = it}

        val color = when{
            diff < 0                -> "#FF4842"
            diff in 0..4            -> "#ff8e42"
            diff in 5..9            -> "#ffc642"
            diff in 10..29          -> "#7bff42"
            else                    -> "#4248ff"
        }

        viewHolder.color_bg.setBackgroundColor(Color.parseColor(color))

//        (task.priority).also { viewHolder.priority.text = it }

        // Criando o handler para quando o item for clicado.
        viewHolder.itemView.setOnClickListener{
            task = itens[viewHolder.bindingAdapterPosition]
            selectedPosition = viewHolder.bindingAdapterPosition
            Toast.makeText(context, "Segure para abrir o menu de opções!", Toast.LENGTH_LONG).show()
        }

        // Menu de ação que será exibido quando ouver um LonClick
        viewHolder.itemView.setOnCreateContextMenuListener { menu, _, _ ->
            menu!!.setHeaderTitle("Ações: ")
            menu.add(0, 0, 0, "Visualizar")
            menu.add(0, 1, 1, "Editar")
            menu.add(0, 2, 2, "Deletar")

            task = itens[viewHolder.bindingAdapterPosition]
            selectedPosition = viewHolder.bindingAdapterPosition
        }
    }

    override fun getItemCount(): Int {
        return itens.size
    }

    // Exclusão visual da tela.
    fun deleteTask(){
        itens.remove(task)
        this.notifyItemRemoved(selectedPosition)
    }
}