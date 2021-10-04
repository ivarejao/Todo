package com.androidapp.todo.activities

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html.fromHtml
import android.widget.Toast
import androidx.annotation.RequiresApi

class UppercaseActivity : Activity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
        val readonly = intent.getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false)

        if(!readonly && text != null){

            val outgoingIntent = Intent()
            outgoingIntent.putExtra(Intent.EXTRA_PROCESS_TEXT, text.toString().toUpperCase())
            setResult(Activity.RESULT_OK, outgoingIntent)

        } else {
            Toast.makeText(this, "Text cannot be modified", Toast.LENGTH_SHORT).show()
        }

        finish()
    }
}

class BoldcaseActivity : Activity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
        val readonly = intent.getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false)

        if(!readonly && text != null){

            val outgoingIntent = Intent()
            var res = fromHtml("<b>$text<b>")
            outgoingIntent.putExtra(Intent.EXTRA_PROCESS_TEXT, res)
            setResult(Activity.RESULT_OK, outgoingIntent)

        } else {
            Toast.makeText(this, "Text cannot be modified", Toast.LENGTH_SHORT).show()
        }

        finish()
    }
}

class ItaliccaseActivity : Activity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
        val readonly = intent.getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false)

        if(!readonly && text != null){

            val outgoingIntent = Intent()
            var res = fromHtml("<i>$text<i>")
            outgoingIntent.putExtra(Intent.EXTRA_PROCESS_TEXT, res)
            setResult(Activity.RESULT_OK, outgoingIntent)

        } else {
            Toast.makeText(this, "Text cannot be modified", Toast.LENGTH_SHORT).show()
        }

        finish()
    }
}

class UnderLinedcaseActivity : Activity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
        val readonly = intent.getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false)

        if(!readonly && text != null){

            val outgoingIntent = Intent()
            var res = fromHtml("<u>$text<u>")
            outgoingIntent.putExtra(Intent.EXTRA_PROCESS_TEXT, res)
            setResult(Activity.RESULT_OK, outgoingIntent)

        } else {
            Toast.makeText(this, "Text cannot be modified", Toast.LENGTH_SHORT).show()
        }

        finish()
    }
}

