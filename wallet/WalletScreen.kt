package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.bitcot.saludnow.client.feature.bottomnav.tab.appointments.appointmentdetails.AppointmentDetailsScreen
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.WalletMainScreen
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.WalletTopUpScreen

@Composable
fun WalletScreen(component: WalletComponent) {
    Children(
        stack = component.childStack,
        animation = stackAnimation(slide())
    ) {
        when (val child = it.instance) {
            is WalletComponent.Child.Main -> WalletMainScreen(child.component)
            is WalletComponent.Child.TopUp -> WalletTopUpScreen(child.component)
            is WalletComponent.Child.AppointmentDetails -> AppointmentDetailsScreen(child.component)
        }
    }
}