package com.example.bancodigital.data.repository.deposit

import com.example.bancodigital.data.model.Deposit
import com.example.bancodigital.util.FirebaseHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class DepositDataSourceImpl @Inject constructor(
    firebaseDatabase: FirebaseDatabase
) : DepositDataSource {

    private val depositReference = firebaseDatabase.reference
        .child("deposit")
        .child(FirebaseHelper.getUserId())

    override suspend fun saveDeposit(deposit: Deposit): Deposit {
        return suspendCoroutine { continuation ->
            depositReference
                .child(deposit.id)
                .setValue(deposit).addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        // Depois de salvar o deposito no Firease é atualizado o valor da data do
                        // deposito e assim que a atualização da data for feita com sucesso é
                        // retornado o deposito com o continuation
                        val dateReference = depositReference
                            .child(deposit.id)
                            .child("date")

                        dateReference.setValue(ServerValue.TIMESTAMP)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    continuation.resumeWith(Result.success(deposit))
                                } else {
                                    task.exception?.let {
                                        continuation.resumeWith(Result.failure(it))
                                    }
                                }
                            }


                    } else {
                        task.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                }
        }
    }

    override suspend fun getDeposit(idDeposit: String): Deposit {
        return suspendCoroutine { continuation ->
            depositReference
                .child(idDeposit)
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val deposit = snapshot.getValue(Deposit::class.java)

                        deposit?.let {
                            continuation.resumeWith(Result.success(it))
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        error.toException().let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }

                })
        }
    }

}