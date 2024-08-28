package com.example.bancodigital.data.repository.recharge

import com.example.bancodigital.data.model.Recharge
import com.example.bancodigital.util.FirebaseHelper
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class RechargeDataSourceImpl @Inject constructor(
    firebaseDatabase: FirebaseDatabase
) : RechargeDataSource {

    private val rechargeReference = firebaseDatabase.reference
        .child("recharge")
        .child(FirebaseHelper.getUserId())

    override suspend fun saveRecharge(recharge: Recharge): Recharge {
        return suspendCoroutine { continuation ->
            rechargeReference
                .child(recharge.id)
                .setValue(recharge).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resumeWith(Result.success(recharge))
                    } else {
                        task.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                }
        }
    }

    override suspend fun getRecharge(id: String): Recharge {
        TODO("Not yet implemented")
    }


}