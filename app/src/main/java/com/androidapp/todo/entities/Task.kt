package com.androidapp.todo.entities

import androidx.room.*
import java.util.*

@Entity
class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "title")
    var title: String?,

    @ColumnInfo(name = "subtitle")
    var subtitle: String?,

    @ColumnInfo(name = "text")
    var text: String?,

    @TypeConverters(DateConverter::class)
    @ColumnInfo(name = "date")
    var date: Date?,

    @ColumnInfo(name = "sync")
    var sync: Boolean?

)