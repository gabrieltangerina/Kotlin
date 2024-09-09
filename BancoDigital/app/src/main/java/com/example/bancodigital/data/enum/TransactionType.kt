package com.example.bancodigital.data.enum

enum class TransactionType {
    CASH_IN, CASH_OUT;

    companion object {
        fun getType(operation: TransactionOperation): Char {
            when (operation) {
                TransactionOperation.DEPOSIT -> {
                    return 'D'
                }

                TransactionOperation.RECHARGE -> {
                    return 'R'
                }

                TransactionOperation.TRANSFER -> {
                    return 'T'
                }
            }
        }
    }

}