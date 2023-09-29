package com.example.quizgamemocktest.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.quizgamemocktest.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // hide action bar
        supportActionBar?.hide()

        auth = Firebase.auth
        Handler(Looper.getMainLooper()).postDelayed({

            val user = auth.currentUser
            if(user != null){

                Toast.makeText(this, "Welcome to Quiz game, ${user.email}", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)

                //use to destroy current activity
                finish()
            }
            else {
                val intent = Intent(this, SignInMainActivity::class.java)
                startActivity(intent)
                //use to destroy current activity
                finish()
            }

        },5000)
    }
}