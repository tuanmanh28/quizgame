package com.example.quizgamemocktest.viewmodel


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.quizgamemocktest.databinding.ActivitySignInMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivitySignInMainBinding
class SignInMainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // hide Action Bar
        supportActionBar?.hide()
        binding = ActivitySignInMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // button sign up
        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        // button forget password
        binding.btnForgetPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }

        auth = Firebase.auth
        binding.btnSignIn.setOnClickListener {
            //if true
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            if(checkAllField()){
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                    if(task.isSuccessful){
                        // if sign in is successful
                        Toast.makeText(this, "Sign in Successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        // use to destroy activity
                        finish()
                    }
                    else{
                        Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show()
                        Toast.makeText(this, "Please check your account or your password", Toast.LENGTH_SHORT).show()
                        binding.edtPassword.setText("")
                        Log.e("error: ",task.exception.toString())
                    }
                }
            }
        }

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("298532066488-t5h9m8qodqh2s6ohs75j1t4pqpjh8jqg.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.btnContinueWithGoogle.setOnClickListener {
            signInWithGoogle()
        }
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


    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            result(task)
        }
    }

    private fun result(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful){
            val account: GoogleSignInAccount?= task.result
            if(account != null){
                updateSomeUI(account)
            }
        }
    }

    private fun updateSomeUI(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful){

                val user = auth.currentUser
                Toast.makeText(this, "Welcome to Quiz game, ${user?.email}", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
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

        return true
    }

}