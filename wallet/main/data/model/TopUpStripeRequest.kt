package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopUpStripeRequest(
    @SerialName("stripe_credit_card_id") val stripeCardId: String,
    @SerialName("amount") val amount: Int,
)
