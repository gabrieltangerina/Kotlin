package com.example.imageview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.imageview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var flag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            if(flag){
                binding.imageView.setImageResource(R.drawable.baseline_visibility_24)
            }else{
                binding.imageView.setImageResource(R.drawable.baseline_visibility_off_24)
            }

            flag = !flag
        }
    }
}