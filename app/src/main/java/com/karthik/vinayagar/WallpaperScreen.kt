package com.karthik.vinayagar

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager

import kotlinx.android.synthetic.main.wallpaper_layout.*

class WallpaperScreen : AppCompatActivity(), WallpaperAdapter.OnItemClickListener {

    val adapter = WallpaperAdapter(WallpaperImages,this)

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wallpaper_layout)



        wallpaperRecView.adapter = adapter
        wallpaperRecView.layoutManager = LinearLayoutManager(this)
        wallpaperRecView.setHasFixedSize(true)




    }
    var clicked = false

    override fun onItemClick(position: Int) {
        if (!clicked) {
            val intent = Intent(this, FullWallpaperScreen::class.java)
            intent.putExtra("image", position)
            startActivity(intent)
            clicked = true
        }

    }

    override fun onResume() {
        super.onResume()
        clicked = false

    }

}