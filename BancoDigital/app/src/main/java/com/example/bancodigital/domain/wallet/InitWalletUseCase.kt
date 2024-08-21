package com.example.bancodigital.domain.wallet

import com.example.bancodigital.data.model.Wallet
import com.example.bancodigital.data.repository.wallet.WalletDataSourceImpl
import javax.inject.Inject

class InitWalletUseCase @Inject constructor(
    private val walletDataSourceImpl: WalletDataSourceImpl
) {

    suspend operator fun invoke(wallet: Wallet) {
        return walletDataSourceImpl.initWallet(wallet)
    }

}