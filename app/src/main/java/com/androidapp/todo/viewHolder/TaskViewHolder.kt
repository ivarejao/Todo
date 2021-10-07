package com.androidapp.todo.viewHolder

import android.view.ContextMenu
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.androidapp.todo.R

/**
 * Classe para acessar os elementos do RecyclerView.
 */
class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    , View.OnLongClickListener, View.OnClickListener, View.OnCreateContextMenuListener{

    /**
     * Título de um elemento da lista.
     */
    val title: TextView = itemView.findViewById(R.id.title)

    /**
     * Subtitulo de um elemento da lista.
     */
    val subtitle: TextView = itemView.findViewById(R.id.subtitle)

    /**
     * Data de um elementod a lista.
     */
    val date: TextView = itemView.findViewById(R.id.date)

    /**
     * Tempo restante de um elemento da lista.
     */
    val timeRemaining: TextView = itemView.findViewById(R.id.timeRemaining)

    /**
     * Cor do card de um elemento da lista.
     */
    val colorBg: ConstraintLayout = itemView.findViewById(R.id.background_task)

    /**
     * Função que habilita o long click em um item do recyclerView.
     *
     * @param v a View do objeto clicado.
     * @return true
     */
    override fun onLongClick(v: View?): Boolean {
        return true
    }

    /**
     * Função de clique sobre um dado item do recyclerView, será sobrescrita no adapter.
     *
     * @param v a View do objeto clicado.
     */
    override fun onClick(v: View?) {
    }

    /**
     * Função que cria o menu de ações, será sobrescrita no adapter.
     */
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menu!!.setHeaderTitle("Ações: ")
        menu.add(0, 0, 0, "Visualizar")
        menu.add(0, 1, 1, "Editar")
        menu.add(0, 2, 2, "Deletar")
    }
}