package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.main.domain.usecase

import com.bitcot.saludnow.client.core.extensions.orZero
import com.bitcot.saludnow.client.core.network.util.Result
import com.bitcot.saludnow.client.core.network.util.RootError
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.model.TopUpStripeWallet
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.repository.WalletRepository
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.main.domain.model.TopUpMethod

class TopUpWalletUseCase(
    private val walletRepository: WalletRepository,
) {
    suspend operator fun invoke(
        amount: String,
        topUpMethod: TopUpMethod,
        onLoading: () -> Unit,
        onSuccess: () -> Unit,
        onOxxoPaymentSecretSuccess: (paymentSecret: String) -> Unit,
        onError: (RootError) -> Unit,
    ) {
        val castedAmount = amount.toIntOrNull().orZero()

        when (topUpMethod) {
            is TopUpMethod.Card -> {
                walletRepository.topUpWithCard(
                    TopUpStripeWallet(topUpMethod.id, castedAmount)
                ).collect {
                    when (it) {
                        Result.Loading -> onLoading()
                        is Result.Success -> onSuccess()
                        is Result.Error -> onError(it.cause)
                    }
                }
            }

            TopUpMethod.Oxxo -> {
                walletRepository.topUpWithOxxo(castedAmount).collect {
                    when (it) {
                        Result.Loading -> onLoading()
                        is Result.Success -> onOxxoPaymentSecretSuccess(it.value.paymentSecret)
                        is Result.Error -> onError(it.cause)
                    }
                }
            }

            TopUpMethod.None -> {}
        }
    }
}