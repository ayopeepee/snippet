package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnResume
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.store.WalletMainStore
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.store.WalletMainStoreFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class WalletMainComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val output: (Output) -> Unit,
) : ComponentContext by componentContext {

    private val walletMainStore = instanceKeeper.getStore {
        WalletMainStoreFactory(
            storeFactory = storeFactory,
        ).create()
    }

    init {
        lifecycle.doOnResume {
            walletMainStore.accept(WalletMainStore.Intent.ReloadPage)
        }
    }

    fun onIntent(intent: WalletMainStore.Intent) {
        walletMainStore.accept(intent)
    }

    fun onOutput(output: Output) {
        output(output)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<WalletMainStore.State> = walletMainStore.stateFlow

    sealed interface Output {
        data object NavigateBack : Output
        data object NavigateToTopUp : Output
        data class NavigateToAppointmentDetails(val appointmentId: Long) : Output
    }
}