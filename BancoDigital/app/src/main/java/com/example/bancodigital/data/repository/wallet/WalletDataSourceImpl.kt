package com.example.bancodigital.data.repository.wallet

import com.example.bancodigital.data.enum.TransactionType
import com.example.bancodigital.data.model.Transaction
import com.example.bancodigital.data.model.Wallet
import com.example.bancodigital.util.FirebaseHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class WalletDataSourceImpl @Inject constructor(
    database: FirebaseDatabase
) : WalletDataSource {

    private val walletReference = database.reference
        .child("wallet")

    private val transactionsReference = database.reference
        .child("transaction")

    override suspend fun initWallet(wallet: Wallet) {
        return suspendCoroutine { continuation ->
            walletReference
                .child(FirebaseHelper.getUserId())
                .setValue(wallet).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resumeWith(Result.success(Unit))
                    } else {
                        task.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                }
        }
    }

    override suspend fun getWallet(): Wallet {
        return suspendCoroutine { continuation ->
            walletReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val wallet =
                        snapshot.child(FirebaseHelper.getUserId()).getValue(Wallet::class.java)

                    wallet?.let {
                        continuation.resumeWith(Result.success(it))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWith(Result.failure(error.toException()))
                }

            })
        }
    }

    override suspend fun getBalance(): Float {
        return suspendCoroutine { continuation ->
            transactionsReference
                .child(FirebaseHelper.getUserId())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val transactions = mutableListOf<Transaction>()
                        var cashIn = 0f
                        var cashOut = 0f

                        for (ds in snapshot.children) {
                            val transaction = ds.getValue(Transaction::class.java)

                            transaction?.let {
                                transactions.add(it)
                            }
                        }

                        transactions.forEach { transaction ->
                            if (transaction.type == TransactionType.CASH_IN) {
                                cashIn += transaction.amount
                            } else {
                                cashOut += transaction.amount
                            }
                        }

                        continuation.resumeWith(Result.success(cashIn - cashOut))
                    }

                    override fun onCancelled(error: DatabaseError) {
                        continuation.resumeWith(Result.failure(error.toException()))
                    }

                })
        }
    }

}