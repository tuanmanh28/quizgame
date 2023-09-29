package com.example.quizgamemocktest.viewmodel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.quizgamemocktest.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private lateinit var binding: ActivityHomeBinding
class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth



        binding.btnStartGame.setOnClickListener {
            val intent = Intent(this, LoadingActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSignOut.setOnClickListener {
            // use to sign out account
            auth.signOut()

            Toast.makeText(this, "Sign out successfully!", Toast.LENGTH_SHORT).show()
            // back to sign in activity
            val intent = Intent(this, SignInMainActivity::class.java)
            startActivity(intent)
            // finish Home activity
            finish()
        }
    }
}