package com.example.uniplanner

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import com.example.uniplanner.databinding.ActivitySplashBinding



class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load animations from res/anim/hover.xml
        val hoverAnimation = AnimationUtils.loadAnimation(this, R.anim.hover)

        // Apply animations to views
        binding.logoImageView.startAnimation(hoverAnimation)
        binding.appNameTextView.startAnimation(hoverAnimation)
        binding.taglineTextView.startAnimation(hoverAnimation)
        binding.noteTextView.startAnimation(hoverAnimation)
        binding.footerTextView.startAnimation(hoverAnimation)

        // Delay to start the main activity after the splash screen
        binding.root.postDelayed({
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 5000) // 5 seconds delay
    }
}
