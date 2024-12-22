package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.component.WalletMainContent

@Composable
fun WalletMainScreen(component: WalletMainComponent) {

    val state by component.state.collectAsState()

    WalletMainContent(state = state, onIntent = component::onIntent, onOutput = component::onOutput)
}