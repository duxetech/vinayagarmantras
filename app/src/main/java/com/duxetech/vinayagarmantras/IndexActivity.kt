package com.duxetech.vinayagarmantras

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_index.*
import kotlinx.android.synthetic.main.activity_main.toolbar

class IndexActivity : AppCompatActivity(), ExampleAdapter.OnItemClickListener {

    var size = 16F
    val adapter = ExampleAdapter(chapters,this)
    lateinit var mAdView: AdView
    var mediaPlayer : MediaPlayer? = null
    var track = 0
    var handler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_index)
        setSupportActionBar(toolbar)
        var toolbar : androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        var toolbarTextview : TextView = findViewById(R.id.toolbar_text)
        toolbarTextview.text = title
        setSupportActionBar(toolbar)

        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        loadSong()

        chaptersRecView.adapter = adapter
        chaptersRecView.layoutManager=LinearLayoutManager(this)
        chaptersRecView.setHasFixedSize(true)
        progressBar.        isVisible = false

        trackTitle.isSelected = true

        nextButton.setOnClickListener {

            loadSong()
            play()
        }

        previousButton.setOnClickListener{
            mediaPlayer!!.reset()
            mediaPlayer!!.release()
            loadSong()
            play()
        }

        playButton.setOnClickListener {
            play()
        }

        mediaPlayer?.setOnCompletionListener(MediaPlayer.OnCompletionListener { // Do something when media player end playing
            progressBar.isVisible = false
            playButton.setBackgroundResource(R.drawable.play)
        })

        progressBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        mediaPlayer?.seekTo(progress*1000)
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
                    val mCurrentPosition: Int = (mediaPlayer!!.currentPosition / 1000)
                    progressBar.progress = mCurrentPosition
                }
                handler.postDelayed(this, 1000)
            }
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (mediaPlayer==null) {
            return
        }
        runOnUiThread {
            mediaPlayer!!.stop()
            mediaPlayer!!.reset()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }


    fun play() {
        if (mediaPlayer?.isPlaying!!) {
            mediaPlayer!!.pause()
            playButton.setBackgroundResource(R.drawable.button_style_play)
            progressBar.isVisible = false
        } else {
            mediaPlayer!!.start()
            playButton.setBackgroundResource(R.drawable.button_style_pause)
            progressBar.isVisible = true

        }
        mediaPlayer!!.setOnCompletionListener {
            loadSong()
            play()
        }

    }

    fun loadSong() {
        if (mediaPlayer!=null) {
            mediaPlayer!!.reset()
            mediaPlayer!!.release()
        }
        mediaPlayer = MediaPlayer.create(applicationContext, songs[track])
        progressBar.max = mediaPlayer!!.duration / 1000
        trackTitle.text = songNames[track]

        if (track+1 == songs.size) {
            track = 0
        } else {
            track++
        }

    }
    override fun onItemClick(position: Int) {

        val intent = Intent(this,ContentScreen::class.java)
        intent.putExtra("chapter",position)
        startActivity(intent)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

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


}