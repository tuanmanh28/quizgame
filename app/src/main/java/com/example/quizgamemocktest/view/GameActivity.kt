package com.example.quizgamemocktest.view

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.quizgamemocktest.R
import com.example.quizgamemocktest.databinding.ActivityGameBinding
import com.example.quizgamemocktest.databinding.FinishDialogBinding
import com.example.quizgamemocktest.model.Questions
import com.example.quizgamemocktest.model.Scores
import com.example.quizgamemocktest.model.Time
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.DateFormat
import java.util.Calendar

private lateinit var binding: ActivityGameBinding
class GameActivity : AppCompatActivity() {
    private var correct = 0
    private var wrong = 0
    private var answer = ""
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var dbReference: DatabaseReference
    private var time = ""
    private lateinit var dialog:AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnFinish.isEnabled = false
        binding.btnFinish.setOnClickListener {
            showFinishDialog()
        }

        val calendar = Calendar.getInstance().time
        time = DateFormat.getDateInstance().format(calendar) + " ; "+ DateFormat.getTimeInstance().format(calendar)
        dbReference = FirebaseDatabase.getInstance().getReference("scores")
        dbReference.child(time).child("correct").setValue(0)
        dbReference.child(time).child("wrong").setValue(0)

        val bundle = intent.extras

        binding.time = Time("25")
        binding.scores = Scores(correct.toString(),wrong.toString())

        var number: Int = 0
        var i = 0
        var a = ""
        var b = ""
        var c = ""
        var d = ""
        countDownTimer = object : CountDownTimer(25100,1000){
            override fun onTick(p0: Long) {
                val second = p0/1000
                binding.time = Time(second.toString())
            }
            override fun onFinish() {
                binding.questions = Questions(
                    "Sorry, Time is up! Continue with next question.",a,b,c,d
                )
                setButtonState(false)
                countDownTimer.cancel()
            }
        }
        fun getInfo(){
            i++
            if ((i<= number)||(i == 1)){
                bundle?.let {
                    number = it.getString("Number").toString().toInt()
                    val question = it.getString("$i question").toString()
                    a = it.getString("$i a").toString()
                    b = it.getString("$i b").toString()
                    c = it.getString("$i c").toString()
                    d = it.getString("$i d").toString()
                    answer = it.getString("$i answer").toString()
                    binding.questions = Questions(question, a, b, c, d, answer)
                    setButtonState(true)
                    binding.btnA.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    binding.btnB.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    binding.btnC.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    binding.btnD.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                }
                countDownTimer.start()
                if (i == number){
                    binding.btnFinish.isEnabled =true
                }
            }
            else {
                showFinishDialog()
            }
        }
        binding.btnNext.setOnClickListener {
            getInfo()
        }
        getInfo()
        btnSetOnClickListener(binding.btnA, "a")
        btnSetOnClickListener(binding.btnB, "b")
        btnSetOnClickListener(binding.btnC, "c")
        btnSetOnClickListener(binding.btnD, "d")
    }

    private fun showFinishDialog() {
        val build = AlertDialog.Builder(this,R.style.FinishDialogTheme)
        val dialogBinding = FinishDialogBinding.inflate(layoutInflater)
        build.setView(dialogBinding.root)
        dialogBinding.btnPlayAgain.setOnClickListener {
            val intent = Intent(this, LoadingActivity::class.java)
            startActivity(intent)
            dialog.dismiss()
            finish()
        }
        dialogBinding.btnSeeResult.setOnClickListener {
            val intent = Intent(this, ResultActivity::class.java)
            val bundle = Bundle()
            bundle.putString("correct",correct.toString())
            bundle.putString("wrong",wrong.toString())
            intent.putExtras(bundle)
            startActivity(intent)
            dialog.dismiss()
            finish()
        }
        dialog = build.create()
        dialog.show()

    }

    private fun setButtonState(isEnable: Boolean) {
        binding.btnA.isEnabled = isEnable
        binding.btnB.isEnabled = isEnable
        binding.btnC.isEnabled = isEnable
        binding.btnD.isEnabled = isEnable
    }

    private fun btnSetOnClickListener(btn: AppCompatButton, choiceAnswer: String) {
        btn.setOnClickListener {
            setButtonState(false)
            countDownTimer.cancel()

            if (choiceAnswer == answer){
                btn.setBackgroundColor(ContextCompat.getColor(this,R.color.green))
                correct++
                dbReference.child(time).child("correct").setValue(correct)
                binding.scores = Scores(correct.toString(),wrong.toString())
            }
            else{
                btn.setBackgroundColor(ContextCompat.getColor(this,R.color.red))
                wrong++
                dbReference.child(time).child("wrong").setValue(wrong)
                binding.scores = Scores(correct.toString(),wrong.toString())
                when(answer){
                    "a" ->{
                        binding.btnA.setBackgroundColor(ContextCompat.getColor(this,R.color.green))
                    }
                    "b" ->{
                        binding.btnB.setBackgroundColor(ContextCompat.getColor(this,R.color.green))
                    }
                    "c" ->{
                        binding.btnC.setBackgroundColor(ContextCompat.getColor(this,R.color.green))
                    }
                    else ->{
                        binding.btnD.setBackgroundColor(ContextCompat.getColor(this,R.color.green))
                    }
                }
            }
        }
    }
}

