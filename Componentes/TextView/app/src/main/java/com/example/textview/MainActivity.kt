package com.example.textview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.textview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nome = "Gabriel Tangerina"

        binding.textView.text = nome
    }
}