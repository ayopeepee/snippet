package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.usecase

import com.bitcot.saludnow.client.core.network.util.Result
import com.bitcot.saludnow.client.core.network.util.RootError
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.repository.WalletRepository

class GetWalletBalanceUseCase(
    private val walletRepository: WalletRepository,
) {
    suspend operator fun invoke(
        onLoading: () -> Unit,
        onSuccess: (balance: String) -> Unit,
        onError: (RootError) -> Unit,
    ) {
        walletRepository.getBalance().collect {
            when (it) {
                Result.Loading -> onLoading()
                is Result.Success -> {
                    // Remove trailing zeros
                    val balance = it.value.balance.toString()
                        .replace(Regex("0*$"), "")
                        .replace(Regex("\\.$"), "")

                    onSuccess(balance)
                }

                is Result.Error -> onError(it.cause)
            }
        }
    }
}