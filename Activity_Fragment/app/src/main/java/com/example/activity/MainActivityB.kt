package com.example.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.activity.databinding.ActivityMainBBinding

class MainActivityB : AppCompatActivity() {
    private lateinit var binding: ActivityMainBBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getExtra()
        }
    }

    // Recebendo dados da ActivityA
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getExtra(){
        if(intent.hasExtra("user")){
            val user = intent.getSerializableExtra("user", User::class.java)

            if (user != null) {
                Toast.makeText(this, "${user.name} ${user.age}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}