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
import kotlinx.android.synthetic.main.activity_main.toolbar
import java.io.FileNotFoundException


class ContentScreen : AppCompatActivity() {

    var content = ""
    var index = 0

    val defText = "*****ஓம் கம் கணபதியே நம*****\n"
    val suffix = " பரிபூரணம் \n"




    lateinit var mAdView: AdView

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)



        index = intent.getIntExtra("chapter",0)


        setSupportActionBar(toolbar)
        var toolbar : androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        var toolbarTextview : TextView = findViewById(R.id.toolbar_text)
        toolbarTextview.text = title
        setSupportActionBar(toolbar)

        contentTitle.text = chapters[index]

        loadFiles(index)

        contentTextView.text = content

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    var size = 16F




    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.wallpaper -> {

                startActivity(Intent(this, WallpaperScreen::class.java))
                true
            }

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
            setText()
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





    fun setText(){
        contentTextView.text = defText+
                content+
                "\n" + "\n"+chapters[index]+suffix+defText
        scrollView.scrollTo(0,0)
        setImage()

    }

    fun setImage() {

        val image = ganeshImages[index]
        titleImage.setImageResource(image)
    }

}
