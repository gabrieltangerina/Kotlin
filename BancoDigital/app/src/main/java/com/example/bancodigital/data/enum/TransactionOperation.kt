package com.example.bancodigital.data.enum

enum class TransactionOperation {
    DEPOSIT;

    companion object {
        fun getOperation(operation: TransactionOperation): String {
            when (operation) {
                DEPOSIT -> {
                    return "DEPÓSITO"
                }
            }
        }
    }
}