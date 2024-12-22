package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.bitcot.saludnow.client.feature.bottomnav.tab.home.appointment.oxxo.OxxoComponent
import com.bitcot.saludnow.client.feature.bottomnav.tab.home.appointment.oxxo.domain.model.OxxoPayment
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.main.WalletTopUpMainComponent
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.main.store.WalletTopUpMainStore
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.success.WalletTopUpSuccessComponent
import kotlinx.serialization.Serializable

class WalletTopUpComponent internal constructor(
    componentContext: ComponentContext,
    private val output: (Output) -> Unit,
    private val main: (ComponentContext, (WalletTopUpMainComponent.Output) -> Unit) -> WalletTopUpMainComponent,
    private val success: (ComponentContext, amount: String, (WalletTopUpSuccessComponent.Output) -> Unit) -> WalletTopUpSuccessComponent,
    private val oxxo: (ComponentContext, payment: OxxoPayment, (OxxoComponent.Output) -> Unit) -> OxxoComponent,
) : ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        output: (Output) -> Unit,
        storeFactory: StoreFactory,
    ) : this(
        componentContext = componentContext,
        output = output,
        main = { childContext, output ->
            WalletTopUpMainComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                output = output,
            )
        },
        success = { childContext, amount, output ->
            WalletTopUpSuccessComponent(
                componentContext = childContext,
                amount = amount,
                output = output,
            )
        },
        oxxo = { childContext, payment, output ->
            OxxoComponent(
                componentContext = childContext,
                payment = payment,
                output = output,
            )
        },
    )

    private val navigation = StackNavigation<Configuration>()

    val childStack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialStack = { listOf(Configuration.Main) },
        key = "WalletTopUpStack",
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

            is Configuration.Success -> Child.Success(
                success(
                    componentContext,
                    configuration.amount,
                    ::onSuccessOutput,
                )
            )

            is Configuration.Oxxo -> Child.Oxxo(
                oxxo(
                    componentContext,
                    configuration.payment,
                    ::onOxxoOutput,
                )
            )
        }

    private fun onMainOutput(output: WalletTopUpMainComponent.Output): Unit =
        when (output) {
            WalletTopUpMainComponent.Output.NavigateBack -> onOutput(Output.NavigateBack)
            is WalletTopUpMainComponent.Output.NavigateToTopUpSuccess -> navigation.push(
                Configuration.Success(output.amount)
            )

            is WalletTopUpMainComponent.Output.NavigateToOxxo -> navigation.push(
                Configuration.Oxxo(output.payment)
            )
        }

    private fun onSuccessOutput(output: WalletTopUpSuccessComponent.Output): Unit =
        when (output) {
            WalletTopUpSuccessComponent.Output.NavigateToProfile -> onOutput(Output.NavigateToMain)
        }

    private fun onOxxoOutput(output: OxxoComponent.Output): Unit =
        when (output) {
            OxxoComponent.Output.NavigateNext -> navigation.pop {
                (childStack.active.instance as? Child.Main)?.component?.onIntent(
                    WalletTopUpMainStore.Intent.UpdateOxxoAttentionBottomSheetVisibility(true)
                )
            }
        }

    fun onOutput(output: Output) {
        output(output)
    }

    sealed interface Output {
        data object NavigateBack : Output
        data object NavigateToMain : Output
    }

    @Serializable
    sealed interface Configuration {

        @Serializable
        data object Main : Configuration

        @Serializable
        data class Success(val amount: String) : Configuration

        @Serializable
        data class Oxxo(val payment: OxxoPayment) : Configuration
    }

    sealed class Child {
        data class Main(val component: WalletTopUpMainComponent) : Child()
        data class Success(val component: WalletTopUpSuccessComponent) : Child()
        data class Oxxo(val component: OxxoComponent) : Child()
    }
}