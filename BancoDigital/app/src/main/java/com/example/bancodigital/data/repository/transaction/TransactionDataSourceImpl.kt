package com.example.bancodigital.data.repository.transaction

import com.example.bancodigital.data.model.Transaction
import com.example.bancodigital.util.FirebaseHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class TransactionDataSourceImpl @Inject constructor(
    firebaseDatabase: FirebaseDatabase
) : TransactionDataSource {

    private val transactionReference = firebaseDatabase.reference
        .child("transaction")
        .child(FirebaseHelper.getUserId())

    override suspend fun saveTransaction(transaction: Transaction) {
        return suspendCoroutine { continuation ->
            transactionReference.child(transaction.id).setValue(transaction)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val dateReference = transactionReference
                            .child(transaction.id)
                            .child("date")

                        dateReference.setValue(ServerValue.TIMESTAMP)

                        continuation.resumeWith(Result.success(Unit))
                    } else {
                        task.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                }
        }
    }

    override suspend fun getTransactions(): List<Transaction> {
        return suspendCoroutine { continuation ->
            transactionReference.addListenerForSingleValueEvent(object : ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                    val transactions = mutableListOf<Transaction>()

                    for(ds in snapshot.children){
                        val transaction = ds.getValue(Transaction::class.java)

                        transaction?.let {
                            transactions.add(it)
                        }
                    }

                    continuation.resumeWith(Result.success(transactions))

                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWith(Result.failure(error.toException()))
                }

            })
        }
    }

}

