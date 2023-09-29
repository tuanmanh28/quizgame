package com.example.quizgamemocktest.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quizgamemocktest.R
import com.example.quizgamemocktest.databinding.ActivityResultBinding
import com.example.quizgamemocktest.model.Scores

private lateinit var binding: ActivityResultBinding
class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle = intent.extras
        bundle?.let{
            val correct = bundle.getString("correct").toString()
            val wrong = bundle.getString("wrong").toString()
            binding.scores = Scores(correct,wrong)
        }
        binding.btnPlayAgain.setOnClickListener{
            val intent = Intent(this, LoadingActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnExit.setOnClickListener {
            val intent = Intent(this, HomeActivity:: class.java)
            startActivity(intent)
            finish()
        }

    }
}