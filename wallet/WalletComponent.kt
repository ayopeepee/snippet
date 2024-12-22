package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.bitcot.saludnow.client.feature.bottomnav.tab.appointments.appointmentdetails.AppointmentDetailsComponent
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.WalletMainComponent
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.WalletTopUpComponent
import kotlinx.serialization.Serializable

class WalletComponent internal constructor(
    componentContext: ComponentContext,
    private val output: (Output) -> Unit,
    private val main: (ComponentContext, (WalletMainComponent.Output) -> Unit) -> WalletMainComponent,
    private val topUp: (ComponentContext, (WalletTopUpComponent.Output) -> Unit) -> WalletTopUpComponent,
    private val appointmentDetails: (ComponentContext, appointmentId: Long, (AppointmentDetailsComponent.Output) -> Unit) -> AppointmentDetailsComponent,
) : ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        output: (Output) -> Unit,
        storeFactory: StoreFactory,
    ) : this(
        componentContext = componentContext,
        output = output,
        main = { childContext, output ->
            WalletMainComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                output = output,
            )
        },
        topUp = { childContext, output ->
            WalletTopUpComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                output = output,
            )
        },
        appointmentDetails = { childContext, appointmentId, output ->
            AppointmentDetailsComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                appointmentId = appointmentId,
                output = output,
            )
        },
    )

    private val navigation = StackNavigation<Configuration>()

    val childStack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialStack = { listOf(Configuration.Main) },
        key = "WalletStack",
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun createChild(
        configuration: Configuration,
        componentContext: ComponentContext
    ): Child =
        when (configuration) {
            Configuration.Main -> Child.Main(
                main(
                    componentContext,
                    ::onMainOutput,
                )
            )

            Configuration.TopUp -> Child.TopUp(
                topUp(
                    componentContext,
                    ::onTopUpOutput,
                )
            )

            is Configuration.AppointmentDetails -> Child.AppointmentDetails(
                appointmentDetails(
                    componentContext,
                    configuration.appointmentId,
                    ::onAppointmentDetailsOutput,
                )
            )
        }

    private fun onMainOutput(output: WalletMainComponent.Output): Unit =
        when (output) {
            WalletMainComponent.Output.NavigateBack -> onOutput(Output.NavigateBack)
            WalletMainComponent.Output.NavigateToTopUp -> navigation.push(Configuration.TopUp)
            is WalletMainComponent.Output.NavigateToAppointmentDetails -> navigation.push(
                Configuration.AppointmentDetails(output.appointmentId)
            )
        }

    private fun onTopUpOutput(output: WalletTopUpComponent.Output): Unit =
        when (output) {
            WalletTopUpComponent.Output.NavigateBack -> navigation.pop()
            WalletTopUpComponent.Output.NavigateToMain -> onOutput(Output.NavigateBack)
        }

    private fun onAppointmentDetailsOutput(output: AppointmentDetailsComponent.Output): Unit =
        when (output) {
            AppointmentDetailsComponent.Output.NavigateBack -> navigation.pop()
        }

    fun onOutput(output: Output) {
        output(output)
    }

    sealed interface Output {
        data object NavigateBack : Output
    }

    @Serializable
    sealed interface Configuration {

        @Serializable
        data object Main : Configuration

        @Serializable
        data object TopUp : Configuration

        @Serializable
        data class AppointmentDetails(val appointmentId: Long) : Configuration
    }

    sealed class Child {
        data class Main(val component: WalletMainComponent) : Child()
        data class TopUp(val component: WalletTopUpComponent) : Child()
        data class AppointmentDetails(val component: AppointmentDetailsComponent) : Child()
    }
}