package com.androidapp.todo

import android.view.ContextMenu
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    , View.OnLongClickListener, View.OnClickListener, View.OnCreateContextMenuListener{

    val title: TextView = itemView!!.findViewById(R.id.title)
    val subtitle: TextView = itemView!!.findViewById(R.id.subtitle)
    val date: TextView = itemView!!.findViewById(R.id.date)

    override fun onLongClick(v: View?): Boolean {
        return true
    }

    override fun onClick(v: View?) {
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menu!!.setHeaderTitle("Ações: ")
        menu!!.add(0, 0, 0, "Editar")
        menu!!.add(0, 1, 1, "Deletar")
    }
}