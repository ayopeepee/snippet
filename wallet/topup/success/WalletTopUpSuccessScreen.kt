package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.success

import androidx.compose.runtime.Composable
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.success.component.WalletTopUpSuccessContent

@Composable
fun WalletTopUpSuccessScreen(component: WalletTopUpSuccessComponent) {
    WalletTopUpSuccessContent(amount = component.amount, onOutput = component::onOutput)
}