package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.success

import com.arkivanov.decompose.ComponentContext

class WalletTopUpSuccessComponent(
    componentContext: ComponentContext,
    val amount: String,
    private val output: (Output) -> Unit,
) : ComponentContext by componentContext {

    fun onOutput(output: Output) {
        output(output)
    }

    sealed interface Output {
        data object NavigateToProfile : Output
    }
}