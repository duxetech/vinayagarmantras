package com.duxetech.vinayagarmantras

import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.net.Uri
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
import androidx.core.view.isVisible
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
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
    val suffix = " பரிபூரணம் \n"
    lateinit var scrollView : ScrollView
    var AudioURL =
        "https://file-examples.com/wp-content/uploads/2017/11/file_example_MP3_700KB.mp3"
    var mediaPlayer = MediaPlayer()
    var started = false
    var totalTime = 0
    var track = 0

    var handler = Handler()
    lateinit  var playButton : Button
    lateinit var pregressBar : SeekBar
    lateinit var trackTitle : TextView
    lateinit var mAdView: AdView

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mAdView.adListener = object: AdListener(){
            override fun onAdFailedToLoad(p0: Int) {
                mAdView.visibility = View.GONE
            }
        }

        trackTitle = findViewById(R.id.trackTitle)
        pregressBar = findViewById(R.id.volumeBar)
        pregressBar.isVisible = false

        trackTitle.isSelected = true

        playButton = findViewById(R.id.play)

        var toolbar : androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        var toolbarTextview : TextView = findViewById(R.id.toolbar_text)
        toolbarTextview.text = title
        setSupportActionBar(toolbar)

        nextButton  = findViewById(R.id.next)
        previousButton = findViewById(R.id.previous)
        scrollView = findViewById(R.id.scrollView)



        contentTextView = findViewById(R.id.contentTextView)
        spinner = findViewById(R.id.spinner1)
        loadSong()

        loadFiles(0)

        contentTextView.text = content

        adapter2 = ArrayAdapter(this,R.layout.spinner_layout, chapters)
        spinner1!!.onItemSelectedListener = this
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown)
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

        mediaPlayer.setOnCompletionListener(OnCompletionListener { // Do something when media player end playing
            pregressBar.isVisible = false
            playButton.setBackgroundResource(R.drawable.play)
        })

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
            playButton.setBackgroundResource(R.drawable.button_style_play)
                pregressBar.isVisible = false
            } else {
            mediaPlayer.start()
            playButton.setBackgroundResource(R.drawable.button_style_pause)
            pregressBar.isVisible = true
            val song =  trackTitle.text.toString()
            loadFiles(song)
                spinner.setSelection(chapters.indexOf(song))

        }
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.reset()
            mediaPlayer.release()
            loadSong()
            play()
        }

    }

    fun loadSong() {
        mediaPlayer = MediaPlayer.create(applicationContext, songs[track])
        pregressBar.max = mediaPlayer.duration / 1000
        trackTitle.text = songNames[track]

        if (track+1 == songs.size) {
            track = 0
        } else {
            track++
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    var size = 16F

    override fun onBackPressed() {
        super.onBackPressed()
        runOnUiThread {
            mediaPlayer.stop()
        }
    }



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
                    "விநாயகர் மந்திரங்கள் app https://play.google.com/store/apps/details?id=com.duxetech.vinayagarmantras"
                )
                startActivity(
                    Intent.createChooser(
                        intent,
                        "Share via"
                    )
                )
                true
            }
            R.id.rate -> {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=com.duxetech.vinayagarmantras")
                    )
                )
                true
            }
            R.id.otherApps -> {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://search?q=pub:duxetech")
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

    private fun loadFiles(chapter : String){

        val file = chapter+".txt"
        try {
            content = application.assets.open(file).bufferedReader().
            use {
                it.readText()
            }
            setText()

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
                "\n" + "\n"+chapters[chapter]+suffix+defText
        scrollView.scrollTo(0,0)
        setImage()

    }

    fun setImage() {

        val image = ganesh[chapter]
        titleImage.setImageResource(image)
    }

}
