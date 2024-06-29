package com.example.radiobutton

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.radiobutton.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // group -> radioGroup que foi alterado
        // checkedId -> id do componente marcado
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rbOpcao1 -> {
                    Toast.makeText(applicationContext, "Opção 1", Toast.LENGTH_SHORT).show()
                }

                R.id.rbOpcao2 -> {
                    Toast.makeText(applicationContext, "Opção 2", Toast.LENGTH_SHORT).show() 
                }

                else -> {
                    Toast.makeText(applicationContext, "Opção 3", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

}