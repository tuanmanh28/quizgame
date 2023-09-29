package com.example.quizgamemocktest.view

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizgamemocktest.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private lateinit var binding: ActivitySignUpBinding
class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.btnShowPassword.setOnClickListener {
            binding.edtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            binding.btnShowPassword.visibility = View.GONE
            binding.btnHidePassword.visibility = View.VISIBLE
        }
        binding.btnHidePassword.setOnClickListener {
            binding.edtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.btnShowPassword.visibility = View.VISIBLE
            binding.btnHidePassword.visibility = View.GONE
        }

        binding.btnShowPasswordAgain.setOnClickListener {
            binding.edtPasswordAgain.transformationMethod = HideReturnsTransformationMethod.getInstance()
            binding.btnShowPasswordAgain.visibility = View.GONE
            binding.btnHidePasswordAgain.visibility = View.VISIBLE
        }
        binding.btnHidePasswordAgain.setOnClickListener {
            binding.edtPasswordAgain.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.btnShowPasswordAgain.visibility = View.VISIBLE
            binding.btnHidePasswordAgain.visibility = View.GONE
        }
        binding.btnSignUpMain.setOnClickListener {
            if(checkAllField()){
                val email = binding.edtEmail.text.toString()
                val password = binding.edtPassword.text.toString()
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener (this){task ->
                    // if successfful account is created
                    // is also signed in
                    if (task.isSuccessful){

                        val user = auth.currentUser
                        Toast.makeText(this, "Welcome to Quiz game, ${user?.email}", Toast.LENGTH_SHORT).show()
                        Toast.makeText(this, "Account has been created successfully!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else {
                        // account can't be created
                        Log.e("error: ", task.exception.toString())
                    }
                }
            }
        }
    }
    private fun checkAllField(): Boolean{
        val email = binding.edtEmail.text.toString()
        if(email == ""){
            binding.edtEmail.error = "Please enter email"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.edtEmail.error = "Please check email format"
            return false
        }
        // note that password should be at least 8 characters
        if(binding.edtPassword.length() < 8){
            binding.edtPassword.error = "Password must be at least 8 characters long"
            return false
        }
        if(binding.edtPassword.text.toString() == ""){
            binding.edtPassword.error = "Please enter password"
            return false
        }
        if(binding.edtPassword.text.toString() != binding.edtPasswordAgain.text.toString()){
            binding.edtPasswordAgain.error = "Please enter password again"
            return false
        }

        return true
    }
}