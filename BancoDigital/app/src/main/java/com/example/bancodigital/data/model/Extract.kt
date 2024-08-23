package com.example.bancodigital.data.model

import com.google.firebase.database.FirebaseDatabase

data class Extract(
    var id: String = "",
    var operation: String = "",
    var date: Long = 0,
    var amount: Float = 0f,
    var type: String = ""
) {

    init {
        this.id = FirebaseDatabase.getInstance().reference.push().key ?: ""
    }

}
