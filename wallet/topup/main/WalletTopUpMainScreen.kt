package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.main.component.WalletTopUpMainContent

@Composable
fun WalletTopUpMainScreen(component: WalletTopUpMainComponent) {

    val state by component.state.collectAsState()
    val label = component.label

    WalletTopUpMainContent(
        state = state,
        label = label,
        onIntent = component::onIntent,
        onOutput = component::onOutput
    )
}