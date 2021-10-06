package com.androidapp.todo

import android.view.ContextMenu
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    , View.OnLongClickListener, View.OnClickListener, View.OnCreateContextMenuListener{

    val title: TextView = itemView!!.findViewById(R.id.title)
    val subtitle: TextView = itemView!!.findViewById(R.id.subtitle)
    val date: TextView = itemView!!.findViewById(R.id.date)
    val timeRemaining: TextView = itemView!!.findViewById(R.id.timeRemaining)
    val color_bg: ConstraintLayout = itemView!!.findViewById(R.id.background_task)

    // Função para habilitar o menu de ações.
    override fun onLongClick(v: View?): Boolean {
        return true
    }

    // Função de clique sobre um dado item do recyclerView, será sobrescrita no adapter.
    override fun onClick(v: View?) {
    }

    // Função do menu de ação, será sobrescrita no adapter.
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menu!!.setHeaderTitle("Ações: ")
        menu!!.add(0, 0, 0, "Visualizar")
        menu!!.add(0, 1, 1, "Editar")
        menu!!.add(0, 2, 2, "Deletar")
    }
}