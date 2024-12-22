package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.bitcot.saludnow.client.core.error.onError
import com.bitcot.saludnow.client.core.network.platform.DispatchersProvider
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.model.Operation
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.usecase.GetWalletBalanceUseCase
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.usecase.GetWalletOperationsHistoryUseCase
import com.bitcot.saludnow.client.utils.ContactOpener
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WalletMainStoreFactory(
    private val storeFactory: StoreFactory,
) : KoinComponent {

    private val dispatchers by inject<DispatchersProvider>()
    private val getWalletBalanceUseCase by inject<GetWalletBalanceUseCase>()
    private val getWalletOperationsHistoryUseCase by inject<GetWalletOperationsHistoryUseCase>()
    private val contactOpener by inject<ContactOpener>()

    fun create(): WalletMainStore =
        object : WalletMainStore,
            Store<WalletMainStore.Intent, WalletMainStore.State, WalletMainStore.Label> by storeFactory.create(
                name = "WalletMainStore",
                initialState = WalletMainStore.State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private inner class ExecutorImpl :
        CoroutineExecutor<WalletMainStore.Intent, Unit, WalletMainStore.State, Message, WalletMainStore.Label>(
            dispatchers.main
        ) {
        override fun executeAction(action: Unit, getState: () -> WalletMainStore.State) {
            runActions()
        }

        override fun executeIntent(
            intent: WalletMainStore.Intent,
            getState: () -> WalletMainStore.State
        ) {
            when (intent) {
                is WalletMainStore.Intent.UpdateBalanceBlockVisibility -> dispatch(
                    Message.BalanceBlockVisibilityUpdated(intent.isVisible)
                )

                is WalletMainStore.Intent.OpenWhatsApp -> contactOpener.openWhatsApp(intent.number)
                WalletMainStore.Intent.ReloadPage -> runActions()
            }
        }

        private fun runActions() {
            scope.launch { getBalance() }
            scope.launch { getOperations() }
        }

        private suspend fun getBalance() {
            getWalletBalanceUseCase.invoke(
                onLoading = { dispatch(Message.SetBalanceLoading) },
                onSuccess = { dispatch(Message.BalanceLoaded(it)) },
                onError = { error ->
                    dispatch(Message.BalanceFailed)
                    error.onError(
                        onGeneralError = { publish(WalletMainStore.Label.Error(it.message)) },
                        onServiceUnavailable = { dispatch(Message.SetNoInternet) },
                    )
                }
            )
        }

        private suspend fun getOperations() {
            getWalletOperationsHistoryUseCase.invoke(
                scope = scope,
                onLoading = { dispatch(Message.SetOperationsLoading) },
                onSuccess = { dispatch(Message.OperationsLoaded(it)) },
                onError = { error ->
                    dispatch(Message.OperationsFailed)
                    error.onError(
                        onGeneralError = { publish(WalletMainStore.Label.Error(it.message)) },
                        onServiceUnavailable = { dispatch(Message.SetNoInternet) },
                    )
                }
            )
        }
    }

    private object ReducerImpl : Reducer<WalletMainStore.State, Message> {
        override fun WalletMainStore.State.reduce(msg: Message): WalletMainStore.State =
            when (msg) {
                Message.SetBalanceLoading -> copy(
                    isBalanceLoading = true,
                    isError = false,
                    isNoInternet = false
                )

                Message.SetOperationsLoading -> copy(
                    isOperationsLoading = true,
                    isError = false,
                    isNoInternet = false
                )

                is Message.BalanceLoaded -> copy(isBalanceLoading = false, balance = msg.balance)
                is Message.OperationsLoaded -> copy(
                    isOperationsLoading = false,
                    operations = msg.operations
                )

                Message.BalanceFailed -> copy(isBalanceLoading = false, isError = true)
                Message.OperationsFailed -> copy(isOperationsLoading = false, isError = true)
                is Message.BalanceBlockVisibilityUpdated -> copy(
                    isBalanceBlockVisible = msg.isVisible
                )

                Message.SetNoInternet -> copy(
                    isBalanceLoading = false,
                    isOperationsLoading = false,
                    isNoInternet = true
                )
            }
    }

    private sealed interface Message {
        data object SetBalanceLoading : Message
        data class BalanceLoaded(val balance: String) : Message
        data object BalanceFailed : Message
        data object SetOperationsLoading : Message
        data class OperationsLoaded(val operations: List<Pair<LocalDate, List<Operation>>>) :
            Message

        data object OperationsFailed : Message
        data class BalanceBlockVisibilityUpdated(val isVisible: Boolean) : Message
        data object SetNoInternet : Message
    }
}