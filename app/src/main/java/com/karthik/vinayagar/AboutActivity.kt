package com.karthik.vinayagar

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        var toolbar : androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        var toolbarTextview : TextView = findViewById(R.id.toolbar_text)
        toolbarTextview.text = "Donate"
        setSupportActionBar(toolbar)

        paypal.setOnClickListener {
            val uri = Uri.parse("http://paypal.me/karthikriches")
            val intent = Intent(Intent.ACTION_VIEW,uri)
            startActivity(intent)
        }

        coffee.setOnClickListener {
            val uri = Uri.parse("https://buymeacoffee.com/karthiks")
            val intent = Intent(Intent.ACTION_VIEW,uri)
            startActivity(intent)        }

    }
}
