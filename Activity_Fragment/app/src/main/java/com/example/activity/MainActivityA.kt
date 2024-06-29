package com.example.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.activity.databinding.ActivityMainABinding

class MainActivityA : AppCompatActivity() {

    private lateinit var binding:ActivityMainABinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainABinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
    }

    // Enviando dados da ActivityA para a ActivityB
    private fun initListeners(){
        binding.btnNext.setOnClickListener {
            val intent = Intent(this, MainActivityB::class.java)
            intent.putExtra("user", User("Gabriel Tangerina", 19))
            startActivity(intent)
        }

        binding.btnExemplo2.setOnClickListener {
            resultLauncher.launch(Intent(this, MainActivityC::class.java))
        }
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data

        if(resultCode == RESULT_OK){
            if(data != null){
                if(data.hasExtra("user")){
                    val user = data.getSerializableExtra("user") as User

                    Toast.makeText(this, "${user.name} ${user.age}", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}