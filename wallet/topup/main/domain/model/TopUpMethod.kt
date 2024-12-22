package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.main.domain.model

sealed interface TopUpMethod {
    data object Oxxo : TopUpMethod
    data class Card(val id: String) : TopUpMethod
    data object None : TopUpMethod
}