package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.bitcot.saludnow.client.feature.bottomnav.tab.home.appointment.oxxo.OxxoScreen
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.main.WalletTopUpMainScreen
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.success.WalletTopUpSuccessScreen

@Composable
fun WalletTopUpScreen(component: WalletTopUpComponent) {
    Children(
        stack = component.childStack,
        animation = stackAnimation(slide())
    ) {
        when (val child = it.instance) {
            is WalletTopUpComponent.Child.Main -> WalletTopUpMainScreen(child.component)
            is WalletTopUpComponent.Child.Success -> WalletTopUpSuccessScreen(child.component)
            is WalletTopUpComponent.Child.Oxxo -> OxxoScreen(child.component)
        }
    }
}