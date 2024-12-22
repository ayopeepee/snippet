package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopUpOxxoRequest(
    @SerialName("amount") val amount: Int,
)
