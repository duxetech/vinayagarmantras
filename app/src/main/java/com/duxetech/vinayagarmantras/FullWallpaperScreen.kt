package com.duxetech.vinayagarmantras

import android.app.WallpaperManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.full_wallpaper_screen.*

class FullWallpaperScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.full_wallpaper_screen)
        val id = intent.getIntExtra("image",0)
        val image = WallpaperImages[id]
        wallImage.setImageResource(image)

        wallpaperButton.setOnClickListener {
            val manager = WallpaperManager.getInstance(applicationContext)
            Toast.makeText(this,"Wallpaper set successfully",Toast.LENGTH_SHORT).show()
            manager.setResource(image)
            finish()
        }
    }
}