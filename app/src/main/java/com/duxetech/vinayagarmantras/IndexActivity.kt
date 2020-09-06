package com.duxetech.vinayagarmantras

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.*
import kotlinx.android.synthetic.main.activity_index.*
import kotlinx.android.synthetic.main.activity_main.toolbar


class IndexActivity : AppCompatActivity(), ExampleAdapter.OnItemClickListener {

    var size = 16F
    val adapter = ExampleAdapter(chapters, this)
    lateinit var mAdView: AdView
    var mediaPlayer : MediaPlayer? = null
    var track = 0
    var handler = Handler()
    var interstitialAd = InterstitialAd(this)


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

        progressBar.isVisible = false

        trackTitle.isSelected = true

        nextButton.setOnClickListener {

            forward()
            loadSong()
            play()
        }

        previousButton.setOnClickListener{
            rewind()
            loadSong()
            play()
        }

        playButton.setOnClickListener {
            play()
        }

        progressBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        mediaPlayer?.seekTo(progress * 1000)
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

        interAd()

    }

    override fun onBackPressed() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show()
            interstitialAd.setAdListener(object : AdListener() {
                override fun onAdClosed() {
                    super.onAdClosed()
                    finish()
                }
            })
        } else {
            super.onBackPressed()
        }

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



    fun interAd(){
        interstitialAd.adUnitId = "ca-app-pub-1097464279467471/9090202639"
        interstitialAd.loadAd(AdRequest.Builder().build())
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
            forward()
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


    }

    fun forward(){
        if (track+1 == songs.size) {
            track = 0
        } else {
            track++
        }

    }

    fun rewind(){
        if (track == 0) {
            track = songs.size - 1
        } else {
            track--
        }
    }
    var clicked = false

    override fun onItemClick(position: Int) {

        if (!clicked) {
            val intent = Intent(this, ContentScreen::class.java)
            intent.putExtra("chapter", position)
            startActivity(intent)
            clicked = true
        }



    }

    override fun onResume() {
        super.onResume()
        clicked = false

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        val increase = menu.findItem(R.id.increaseFont)
        val decrease = menu.findItem(R.id.decreaseFont)
        increase.isVisible = false
        decrease.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.wallpaper -> {

                startActivity(Intent(this, WallpaperScreen::class.java))
                true
            }

            R.id.downloadSong -> {

                startActivity(Intent(this, SongsScreen::class.java))
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


}