package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BalanceResponse(
    @SerialName("success") val isSuccess: Boolean,
    @SerialName("message") val message: String? = null,
    @SerialName("balance") val balance: Double? = null,
)
