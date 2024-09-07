package com.example.bancodigital.domain.wallet

import com.example.bancodigital.data.repository.wallet.WalletDataSourceImpl
import javax.inject.Inject

class GetBalanceUseCase @Inject constructor(
    private val walletDataSourceImpl: WalletDataSourceImpl
) {

    suspend operator fun invoke(): Float {
        return walletDataSourceImpl.getBalance()
    }

}