package com.androidapp.todo.dao

import androidx.room.*

import com.androidapp.todo.entities.Task

/**
 * Classe usada para interagir com o banco de dados de tarefas.
 */
@Dao
interface TaskDao {

    /**
     * Função que busca todas as tarefas.
     */
    @Query("select * from task")
    fun getAllTasks(): List<Task>

    /**
     * Função que busca uma tarefa com titulo que se enquadre no parametro passado.
     *
     * @param title Titulo a ser buscado.
     */
    @Query("select * from task where title like :title")
    fun getTaskByName(title: String): Task

    /**
     * Insere um número variável de tarefas no banco de dados de uma só vez.
     *
     * @param tasks Tarefa(s) a serem inseridas.
     */
    @Insert
    fun insertTasks(vararg tasks: Task)

    /**
     * Deleta uma tarefa do banco de dados.
     *
     * @param task Tarega a ser excluida.
     */
    @Delete
    fun deleteTask(task: Task)

    /**
     * Atualiza uma tarefa no banco de dados.
     *
     * @param task Tarefa a ser atualizada.
     */
    @Update
    fun updateTask(task: Task)

}