package com.example.togglebutton

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.togglebutton.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatus()

        binding.toggleButton.setOnCheckedChangeListener { _, _ ->
            setStatus()
        }

    }

    private fun setStatus(){
        binding.textView.text = if (binding.toggleButton.isChecked){
            "O botão está ligado"
        }else{
            "O botão está desligado"
        }
    }
}