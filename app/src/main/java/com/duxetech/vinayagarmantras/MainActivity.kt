package com.duxetech.vinayagarmantras

import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
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
    var AudioURL =
        "https://file-examples.com/wp-content/uploads/2017/11/file_example_MP3_700KB.mp3"
    var mediaPlayer = MediaPlayer()
    var started = false
    var totalTime = 0

    var handler = Handler()
    lateinit  var playButton : Button
    lateinit var pregressBar : SeekBar


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        pregressBar = findViewById(R.id.volumeBar)
        loadSong()

        playButton = findViewById(R.id.play)

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

        nextButton.setOnClickListener {
            mediaPlayer.reset()
            mediaPlayer.release()
            loadSong()
            play()
        }

        previousButton.setOnClickListener{
            mediaPlayer.reset()
            mediaPlayer.release()
            loadSong()
            play()
        }


        playButton.setOnClickListener {
            play()
        }

        pregressBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        mediaPlayer.seekTo(progress*1000)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {


                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }

            }
        )

        runOnUiThread(object : Runnable {
            override fun run() {
                if (mediaPlayer != null) {
                    val mCurrentPosition: Int = (mediaPlayer.currentPosition / 1000)
                    pregressBar.progress = mCurrentPosition
                }
                handler.postDelayed(this, 1000)
            }
        })


    }


    fun play() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            playButton.setBackgroundResource(android.R.drawable.ic_media_play)
        } else {
            mediaPlayer.start()
            playButton.setBackgroundResource(android.R.drawable.ic_media_pause)
            mediaPlayer.isLooping = true

        }

    }
    var track = 0

    fun loadSong() {
        if (track+1 < songs.size) {
            track++
        } else {
            track = 0
        }
        mediaPlayer = MediaPlayer.create(applicationContext, songs[track])
        pregressBar.max = mediaPlayer.duration / 1000
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    var size = 16F

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.increaseFont -> {
                if (size < 25)
                    size += 1F
                contentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
                true
            }
            R.id.decreaseFont -> {
                if (size > 11)
                    size -= 1F
                contentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
                true
            }
            R.id.about -> {


                startActivity(Intent(this, AboutActivity::class.java))
                true
            }
            R.id.share -> {
                var intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    "விநாயகர் மந்திரங்கள் app https://play.google.com/store/apps/details?id=vinayagarmantras&hl=en_IN"
                )
                startActivity(
                    Intent.createChooser(
                        intent,
                        "Share via"
                    )
                )
                true
            }
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
