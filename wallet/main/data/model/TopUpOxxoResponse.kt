package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopUpOxxoResponse(
    @SerialName("success") val isSuccess: Boolean,
    @SerialName("message") val message: String? = null,
    @SerialName("paymentIntent") val paymentIntent: TopUpOxxoResponsePaymentIntent? = null,
)

@Serializable
data class TopUpOxxoResponsePaymentIntent(
    @SerialName("client_secret") val paymentSecret: String,
)
