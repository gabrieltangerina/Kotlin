package com.example.aswitch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aswitch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatus()

        binding.switchMaterial.setOnCheckedChangeListener { _, isChecked ->
            setStatus()
        }
    }

    private fun setStatus(){
        binding.textStatus.text = if( binding.switchMaterial.isChecked){
            "ativo"
        }else{
            "Inativo"
        }

    }
}