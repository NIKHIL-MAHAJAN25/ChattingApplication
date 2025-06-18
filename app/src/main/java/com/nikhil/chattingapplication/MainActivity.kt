package com.nikhil.chattingapplication

import android.content.Intent
import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.caverock.androidsvg.SVG
import com.google.firebase.auth.FirebaseAuth
import com.nikhil.chattingapplication.databinding.ActivityMainBinding
import com.nikhil.chattingapplication.login.LoginActivity
import com.nikhil.chattingapplication.login.SignUpActivity

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()


        val currentUser = auth.currentUser
        if (currentUser != null) {

            startActivity(Intent(this, HostActivity::class.java))
            finish()
        }
        val svg = SVG.getFromResource(this, R.raw.pic45)
        val drawable = PictureDrawable(svg.renderToPicture())
        binding.img1.setImageDrawable(drawable)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
        binding.signup.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
    }
}
