package com.example.checkbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.checkbox.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatus()

        binding.materialCheckBox.setOnCheckedChangeListener { _, _ ->
            setStatus()
        }

    }

    private fun setStatus(){
        binding.textView.text = if(binding.materialCheckBox.isChecked){
            "Ativo"
        }else{
            "Inativo"
        }
    }
}