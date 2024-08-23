package com.example.bancodigital.data.repository.deposit

import com.example.bancodigital.data.model.Deposit
import com.example.bancodigital.util.FirebaseHelper
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class DepositDataSourceImpl @Inject constructor(
    firebaseDatabase: FirebaseDatabase
) : DepositDataSource {

    private val depositReference = firebaseDatabase.reference
        .child("deposit")
        .child(FirebaseHelper.getUserId())

    override suspend fun saveDeposit(deposit: Deposit): String {
        return suspendCoroutine { continuation ->
            depositReference
                .child(deposit.id)
                .setValue(deposit).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resumeWith(Result.success(deposit.id))
                    } else {
                        task.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                }
        }
    }

}