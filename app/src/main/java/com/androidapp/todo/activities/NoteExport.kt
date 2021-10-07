package com.androidapp.todo.activities

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi

/**
 * Classe que permite que um texto selecionado em qualquer lugar possa ser usado para criar uma nota no aplicativo.
 */
class NoteExport : Activity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
        val readonly = intent.getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false)

        if(text != null){

            val outgoingIntent = Intent(applicationContext, MainActivity::class.java)
            outgoingIntent.putExtra(Intent.EXTRA_PROCESS_TEXT, text)
            setResult(Activity.RESULT_OK, outgoingIntent)
            startActivity(outgoingIntent)

        }
    }
}