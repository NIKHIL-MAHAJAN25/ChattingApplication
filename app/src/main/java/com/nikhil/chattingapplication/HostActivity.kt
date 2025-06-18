package com.nikhil.chattingapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nikhil.chattingapplication.databinding.ActivityHostBinding

class HostActivity : AppCompatActivity() {
    lateinit var binding: ActivityHostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityHostBinding.inflate(layoutInflater)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(binding.root)
        val navController = findNavController(R.id.host)
        val bottom=findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottom.itemIconTintList = ContextCompat.getColorStateList(this, R.color.white)
//        bottom.itemTextColor = ContextCompat.getColorStateList(this, R.color.nav_item_color)
        bottom.setBackgroundColor(ContextCompat.getColor(this, R.color.black))

        bottom.setupWithNavController(navController)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}