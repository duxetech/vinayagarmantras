package com.karthik.vinayagar

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.songs_screen.*


class SongsScreen : AppCompatActivity() {

   val repo = FirebaseRepository()
    val firebase = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.songs_screen)

        downloadSong.setOnClickListener {



            if (firebase.currentUser == null) {
                firebase.signInAnonymously().addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this,"Success",Toast.LENGTH_LONG).show()
                    } else {
                       Log.i("result",it.result.toString())
                    }
                }
            } else {

            }

        }

    }

    fun loadSongs(): Task<QuerySnapshot> {
        return firestore
            .collection("Songs")
            .orderBy("id",Query.Direction.ASCENDING)
            .limit(9).get()


    }



}