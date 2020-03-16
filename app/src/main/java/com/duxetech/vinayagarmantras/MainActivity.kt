package com.duxetech.vinayagarmantras

import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi

import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileNotFoundException

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit var contentTextView : TextView
    lateinit var spinner : Spinner
    var content = ""
    var chapter = 0
    lateinit var adapter2 : ArrayAdapter<String>
    lateinit var nextButton : Button
    lateinit var previousButton : Button
    val defText = "*****ஓம் கம் கணபதியே நம*****\n"
    lateinit var scrollView : ScrollView


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        var toolbar : androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        var toolbarTextview : TextView = findViewById(R.id.toolbar_text)
        nextButton  = findViewById(R.id.next)
        previousButton = findViewById(R.id.previous)
        scrollView = findViewById(R.id.scrollView)

        toolbarTextview.text = title
        setSupportActionBar(toolbar)

        contentTextView = findViewById(R.id.contentTextView)
        spinner = findViewById(R.id.spinner1)

        loadFiles(0)

        contentTextView.text = content




        adapter2 = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, chapters)

        spinner1!!.onItemSelectedListener = this


        spinner1!!.adapter = adapter2

        contentTextView!!.movementMethod = ScrollingMovementMethod()


        nextButton.setOnClickListener {
            if ( chapter+1 < chapters.size ) {

            chapter += 1
            loadFiles(chapter)
            setText()
            spinner.setSelection(chapter) }
            else {
                chapter = 0
                loadFiles(chapter)
                setText()
                spinner.setSelection(chapter)
        }

        }

        previousButton.setOnClickListener{
            if ( chapter > 0 )
            chapter -= 1
            loadFiles(chapter)
            setText()
            spinner.setSelection(chapter)
        }
    }




    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadFiles(chapter : Int){

        val file = chapters[chapter]+".txt"

        try {
            content = application.assets.open(file).bufferedReader().
            use {
                it.readText()
            }
        } catch (e: FileNotFoundException) {

        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        chapter = position
        loadFiles(chapter)
        setText()
    }

    fun setText(){
        contentTextView.text = defText+
                content+
                "\n" + defText
        scrollView.scrollTo(0,0)
        setImage()

    }

    fun setImage() {

        val image = ganesh[chapter]
        imageView.setImageResource(image)
    }

}
