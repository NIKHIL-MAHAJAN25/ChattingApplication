package com.nikhil.chattingapplication.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.messaging.FirebaseMessaging
import com.nikhil.chattingapplication.HostActivity
import com.nikhil.chattingapplication.R
import com.nikhil.chattingapplication.databinding.ActivitySignUpBinding
import com.nikhil.chattingapplication.dataclasses.User

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val db= Firebase.firestore
    var collectioname="Users"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnDontsignup.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.btnSignUP.setOnClickListener {
            val email = binding.etmailsignin2.text.toString()
            val password = binding.etpsswrdsignin2.text.toString()
            val name = binding.tvname.text.toString()
            val age = binding.etage.text.toString().toIntOrNull()
            val gender = when {
                binding.radioMale.isChecked -> 1
                binding.radioFemale.isChecked -> 2
                binding.radioothers.isChecked -> 0
                else -> 4
            }

            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                FirebaseMessaging.getInstance().token.addOnSuccessListener {token ->

                    Log.d("FCM_TOKEN","Token:$token")
                    val uid = auth.currentUser?.uid
                    val user =
                        User(userid = uid, name = name, age = age, gender = gender, devicetoken = token )
                    if (uid != null) {
                        db.collection("Users").document(uid).set(user).addOnSuccessListener {
                            Toast.makeText(this, "Signup successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, HostActivity::class.java))
                        }
                    }

                }.addOnFailureListener { e ->
                    Log.e("FCM_TOKEN", "Failed to fetch token", e)
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Signup failure", Toast.LENGTH_SHORT).show()
            }
        }
    }
}