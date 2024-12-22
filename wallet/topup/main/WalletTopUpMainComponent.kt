package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.bitcot.saludnow.client.feature.bottomnav.tab.home.appointment.oxxo.domain.model.OxxoPayment
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.main.store.WalletTopUpMainStore
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.main.store.WalletTopUpStoreFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class WalletTopUpMainComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val output: (Output) -> Unit,
) : ComponentContext by componentContext {

    private val walletTopUpMainStore = instanceKeeper.getStore {
        WalletTopUpStoreFactory(
            storeFactory = storeFactory,
        ).create()
    }

    fun onIntent(intent: WalletTopUpMainStore.Intent) {
        walletTopUpMainStore.accept(intent)
    }

    fun onOutput(output: Output) {
        output(output)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<WalletTopUpMainStore.State> = walletTopUpMainStore.stateFlow
    val label: Flow<WalletTopUpMainStore.Label> = walletTopUpMainStore.labels

    sealed interface Output {
        data object NavigateBack : Output
        data class NavigateToTopUpSuccess(val amount: String) : Output
        data class NavigateToOxxo(val payment: OxxoPayment) : Output
    }
}