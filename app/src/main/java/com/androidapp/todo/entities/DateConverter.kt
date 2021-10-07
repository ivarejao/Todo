package com.androidapp.todo.entities

import androidx.room.TypeConverter
import java.util.*

/**
 * Classe necessária para se armazenar as datas no banco de dados.
 */
class DateConverter {

    /**
     * Recebe um long e o transforma em uma data.
     *
     * @param value Long a ser convertido.
     */
    @TypeConverter
    fun fromTimeStamp(value: Long) : Date? {
        return if(value == null) null else Date(value)
    }

    /**
     * Recebe uma data e o transforma em um long.
     *
     * @param date Data a ser convertida.
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?) : Long? {
        return date?.time
    }
}