package com.androidapp.todo

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.androidapp.todo.entities.Task
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class TaskAdapter(var context: Context, var itens : ArrayList<Task>) : RecyclerView.Adapter<TaskViewHolder>() {

    lateinit var task: Task
    var selectedPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return TaskViewHolder(view)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(viewHolder: TaskViewHolder, position: Int) {

        val format  = SimpleDateFormat("dd/MM/yyyy")

        // Definindo como vai aparecer cada item do recyclerView
        task = itens[viewHolder.bindingAdapterPosition]
        ("Titulo:" + task.title).also { viewHolder.title.text = it }
        ("Subtitulo: " + task.subtitle).also { viewHolder.subtitle.text = it }
        ("Data: " + format.format(task.date!!)).also { viewHolder.date.text = it }

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