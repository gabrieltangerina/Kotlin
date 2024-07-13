package com.example.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sharedpreferences.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences("user", Context.MODE_PRIVATE)

        binding.buttonSave.setOnClickListener {
            saveData()
        }

        binding.buttonRecover.setOnClickListener {
            getData()
        }
    }

    private fun saveData(){
        val name = binding.editName.text.toString().trim()
        val lastName = binding.editLastName.text.toString().trim()

        with (sharedPref.edit()) {
            putString("name", name)
            putString("lastName", lastName)
            apply()
        }

        Toast.makeText(applicationContext, "Dados salvos com sucesso no dispositivo", Toast.LENGTH_SHORT).show()
    }

    private fun getData(){
        val name = sharedPref.getString("name", "")
        val lastName = sharedPref.getString("lastName", "")

       binding.editName.setText(name)
       binding.editLastName.setText(lastName)
    }

}