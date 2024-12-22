package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.model

data class TopUpStripeWallet(
    val stripeCardId: String,
    val amount: Int,
)
