package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.store

import com.arkivanov.mvikotlin.core.store.Store
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.model.Operation
import kotlinx.datetime.LocalDate

interface WalletMainStore :
    Store<WalletMainStore.Intent, WalletMainStore.State, WalletMainStore.Label> {

    data class State(
        val isBalanceLoading: Boolean = false,
        val isOperationsLoading: Boolean = false,
        val isError: Boolean = false,
        val isNoInternet: Boolean = false,
        val balance: String = "",
        val operations: List<Pair<LocalDate, List<Operation>>> = emptyList(),
        val isAboutWalletBottomSheetVisible: Boolean = false,
        val isBalanceBlockVisible: Boolean = true,
    )

    sealed interface Intent {
        data class UpdateBalanceBlockVisibility(val isVisible: Boolean) : Intent
        data class OpenWhatsApp(val number: String) : Intent
        data object ReloadPage : Intent
    }

    sealed interface Label {
        data class Error(val message: String) : Label
    }
}