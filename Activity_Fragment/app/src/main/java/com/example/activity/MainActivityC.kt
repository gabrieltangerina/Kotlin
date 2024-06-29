package com.example.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.activity.databinding.ActivityMainCBinding

class MainActivityC : AppCompatActivity() {

    private lateinit var binding:ActivityMainCBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainCBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    // Devolvendo dados para a Activity
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == android.R.id.home){
            val intent = Intent()
            intent.putExtra("user", User("Gabriel", 19))
            setResult(RESULT_OK, intent)
            finish()
        }


        // return super.onOptionsItemSelected(item)
        return true
    }
}