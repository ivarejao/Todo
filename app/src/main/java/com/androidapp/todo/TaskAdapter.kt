package com.androidapp.todo

import android.content.Context
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.androidapp.todo.entities.Task
import java.text.SimpleDateFormat

class TaskAdapter(var context: Context, var itens : ArrayList<Task>) : RecyclerView.Adapter<TaskViewHolder>() {

    lateinit var task: Task
    var selectedPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        val holder = TaskViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(viewHolder: TaskViewHolder, position: Int) {

        val format  = SimpleDateFormat("dd/MM/yyyy")

        //task = itens.get(viewHolder.adapterPosition)
        task = itens.get(viewHolder.bindingAdapterPosition)
        viewHolder.title.text = task.title
        viewHolder.subtitle.text = task.subtitle
        viewHolder.date.text = format.format(task.date!!)

        viewHolder.itemView.setOnClickListener{
            //task = itens.get(viewHolder.adapterPosition)
            task = itens.get(viewHolder.bindingAdapterPosition)
            //selectedPosition = viewHolder.adapterPosition
            selectedPosition = viewHolder.bindingAdapterPosition
            Toast.makeText(context, "Tarefa clicada " + task.title, Toast.LENGTH_LONG).show()
        }

        viewHolder.itemView.setOnCreateContextMenuListener(object : View.OnCreateContextMenuListener{
            override fun onCreateContextMenu(
                menu: ContextMenu?,
                v: View?,
                menuInfo: ContextMenu.ContextMenuInfo?
            ) {
                menu!!.setHeaderTitle("Ações: ")
                menu!!.add(0, 0, 0, "Editar")
                menu!!.add(0, 1, 1, "Deletar")

                //task = itens.get(viewHolder.adapterPosition)
                task = itens.get(viewHolder.bindingAdapterPosition)
                //selectedPosition = viewHolder.adapterPosition
                selectedPosition = viewHolder.bindingAdapterPosition
            }
        })
    }

    override fun getItemCount(): Int {
        return itens.size
    }

    fun deleteTask(){
        itens.remove(task)
        this.notifyItemRemoved(selectedPosition)
    }
}