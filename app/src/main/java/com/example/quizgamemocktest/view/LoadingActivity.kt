package com.example.quizgamemocktest.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quizgamemocktest.databinding.ActivityLoadingBinding
import com.example.quizgamemocktest.model.Questions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private lateinit var binding: ActivityLoadingBinding
class LoadingActivity : AppCompatActivity() {
    private lateinit var dbRef: DatabaseReference
    private lateinit var ds: ArrayList<Questions>
    private lateinit var intent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.isIndeterminate = true

        dbRef = FirebaseDatabase.getInstance().getReference("questions")


        intent = Intent(this, GameActivity::class.java)
        ds = arrayListOf<Questions>()
        val bundle = Bundle()


        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                ds.clear()
                if (snapshot.exists()){
                    var count = 0
                    for (question in snapshot.children){
                        val questionData: Questions? = question.getValue(Questions::class.java)
                        ds.add(questionData!!)

                        bundle.putString("${question.key} question",questionData.question)
                        bundle.putString("${question.key} a",questionData.a)
                        bundle.putString("${question.key} b",questionData.b)
                        bundle.putString("${question.key} c",questionData.c)
                        bundle.putString("${question.key} d",questionData.d)
                        bundle.putString("${question.key} answer",questionData.answer)
                        count ++
                    }
                    bundle.putString("Number",count.toString())
                    intent.putExtras(bundle)

                    startActivity(intent)

                    //use to destroy current activity
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })


    }
    private fun loadData(){

    }
}
