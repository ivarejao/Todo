package com.androidapp.todo.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

/**
 * Classe da tarefa do aplicativo.
 */
@Entity
class Task(
    /**
     * Id da tarefa no banco de dados.
     * Se gera automaticamente com base nos valores ja existentes no banco de dados.
     */
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    /**
     * Titulo da tarefa.
     */
    @ColumnInfo(name = "title")
    var title: String?,

    /**
     * Subtitulo da tarefa.
     */
    @ColumnInfo(name = "subtitle")
    var subtitle: String?,

    /**
     * Texto da tarefa.
     */
    @ColumnInfo(name = "text")
    var text: String?,

    /**
     * Data da tarefa.
     * Usa a classe DateConverter.
     */
    @TypeConverters(DateConverter::class)
    @ColumnInfo(name = "date")
    var date: Date?,

    /**
     * Boolean indicando se a tarefa será sincronizada ou não.
     */
    @ColumnInfo(name = "sync")
    var sync: Boolean?,

)