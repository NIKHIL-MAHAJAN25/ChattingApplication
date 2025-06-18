package com.nikhil.chattingapplication.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.nikhil.chattingapplication.HostActivity
import com.nikhil.chattingapplication.R
import com.nikhil.chattingapplication.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private var auth:FirebaseAuth=FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnSignIn.setOnClickListener {
            val email=binding.etmailsignin2.text.toString()
            val password=binding.etpsswrdsignin2.text.toString()
            auth.signInWithEmailAndPassword(
                email,
                password
            ).addOnSuccessListener {
                startActivity(Intent(this, HostActivity::class.java))
                Toast.makeText(this, "successfull", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "failure", Toast.LENGTH_SHORT).show()
            }
        }
    }
}