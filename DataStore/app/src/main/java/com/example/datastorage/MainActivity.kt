package com.example.datastorage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.example.datastorage.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userManager = UserManager(this)
        initListeners()
    }

    private fun initListeners(){
        binding.btnSave.setOnClickListener {
            val name = binding.editName.text.toString().trim()
            val age = binding.editIdade.text.toString().trim().toInt()
            val authenticated = binding.cbAuthenticated.isChecked

            lifecycleScope.launch {
                userManager.saveUserData(name, age, authenticated)
            }

        }

        binding.btnRecover.setOnClickListener {
            lifecycleScope.launch {
                val user = userManager.getUserData()

                binding.editName.setText(user.name)
                binding.editIdade.setText(user.age.toString())
                binding.cbAuthenticated.isChecked = user.authenticated
            }
        }
    }
}