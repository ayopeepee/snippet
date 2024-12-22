package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.main.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.bitcot.saludnow.client.core.constants.string.EMPTY_STRING
import com.bitcot.saludnow.client.core.error.Error
import com.bitcot.saludnow.client.core.error.onError
import com.bitcot.saludnow.client.core.network.platform.DispatchersProvider
import com.bitcot.saludnow.client.core.network.util.Result
import com.bitcot.saludnow.client.feature.auth.validation.EmailValidator
import com.bitcot.saludnow.client.feature.bottomnav.tab.home.appointment.confirm.domain.card.model.Card
import com.bitcot.saludnow.client.feature.bottomnav.tab.home.appointment.confirm.domain.usecase.AddCardUseCase
import com.bitcot.saludnow.client.feature.bottomnav.tab.home.appointment.confirm.domain.usecase.GetCardsUseCase
import com.bitcot.saludnow.client.feature.bottomnav.tab.home.appointment.confirm.domain.usecase.GetHolderNameUseCase
import com.bitcot.saludnow.client.feature.bottomnav.tab.home.appointment.confirm.domain.usecase.UpdateUserEmailUseCase
import com.bitcot.saludnow.client.feature.bottomnav.tab.home.appointment.confirm.domain.usecase.UpdateUserNamesUseCase
import com.bitcot.saludnow.client.feature.bottomnav.tab.home.appointment.oxxo.domain.model.OxxoPayment
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.main.domain.repository.UserProfileRepository
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.main.domain.model.TopUpMethod
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.main.domain.usecase.TopUpWalletUseCase
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WalletTopUpStoreFactory(
    private val storeFactory: StoreFactory,
) : KoinComponent {

    private val dispatchers by inject<DispatchersProvider>()
    private val userProfileRepository by inject<UserProfileRepository>()
    private val getCardsUseCase by inject<GetCardsUseCase>()
    private val addCardUseCase by inject<AddCardUseCase>()
    private val getHolderNameUseCase by inject<GetHolderNameUseCase>()
    private val topUpWalletUseCase by inject<TopUpWalletUseCase>()
    private val updateUserNamesUseCase by inject<UpdateUserNamesUseCase>()
    private val updateUserEmailUseCase by inject<UpdateUserEmailUseCase>()

    fun create(): WalletTopUpMainStore =
        object : WalletTopUpMainStore,
            Store<WalletTopUpMainStore.Intent, WalletTopUpMainStore.State, WalletTopUpMainStore.Label> by storeFactory.create(
                name = "WalletTopUpStore",
                initialState = WalletTopUpMainStore.State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private inner class ExecutorImpl :
        CoroutineExecutor<WalletTopUpMainStore.Intent, Unit, WalletTopUpMainStore.State, Message, WalletTopUpMainStore.Label>(
            dispatchers.main
        ) {
        override fun executeAction(action: Unit, getState: () -> WalletTopUpMainStore.State) {
            runActions()
        }

        override fun executeIntent(
            intent: WalletTopUpMainStore.Intent,
            getState: () -> WalletTopUpMainStore.State
        ) {
            when (intent) {
                is WalletTopUpMainStore.Intent.TopUpMethodUpdated -> dispatch(
                    Message.TopUpMethodUpdated(topUpMethod = intent.topUpMethod)
                )

                is WalletTopUpMainStore.Intent.UpdateAmount -> dispatch(
                    Message.AmountUpdated(amount = intent.amount)
                )

                WalletTopUpMainStore.Intent.TopUp -> scope.launch {
                    when (getState().selectedTopUpMethod) {
                        is TopUpMethod.Card -> {
                            topUp(
                                amount = getState().amount,
                                topUpMethod = getState().selectedTopUpMethod,
                                firstName = getState().firstName,
                                lastName = getState().lastName,
                                email = getState().email,
                            )
                        }

                        TopUpMethod.Oxxo -> {
                            when {
                                getState().isEmailValid.not() -> dispatch(
                                    Message.OxxoEmailBottomSheetVisibilityUpdated(
                                        true
                                    )
                                )

                                getState().firstName.isBlank() && getState().lastName.isBlank() -> dispatch(
                                    Message.OxxoFullNameBottomSheetVisibilityUpdated(true)
                                )

                                else -> topUp(
                                    amount = getState().amount,
                                    topUpMethod = getState().selectedTopUpMethod,
                                    firstName = getState().firstName,
                                    lastName = getState().lastName,
                                    email = getState().email,
                                )
                            }
                        }

                        TopUpMethod.None -> {}
                    }
                }

                is WalletTopUpMainStore.Intent.UpdateAddCardBottomSheetVisibility -> dispatch(
                    Message.AddCardBottomSheetVisibilityUpdated(isVisible = intent.isVisible)
                )

                is WalletTopUpMainStore.Intent.UpdateCardNumber -> dispatch(
                    Message.CardNumberUpdated(cardNumber = intent.cardNumber)
                )

                is WalletTopUpMainStore.Intent.UpdateHolderName -> dispatch(
                    Message.HolderNameUpdated(name = intent.holderName)
                )

                is WalletTopUpMainStore.Intent.UpdateValidThru -> dispatch(
                    Message.ValidThruUpdated(validThru = intent.validThru)
                )

                is WalletTopUpMainStore.Intent.UpdateCvc -> dispatch(
                    Message.CvcUpdated(cvc = intent.cvc)
                )

                WalletTopUpMainStore.Intent.ClearCardInputs -> {
                    dispatch(Message.CardNumberUpdated(EMPTY_STRING))
                    dispatch(Message.HolderNameUpdated(EMPTY_STRING))
                    dispatch(Message.ValidThruUpdated(EMPTY_STRING))
                    dispatch(Message.CvcUpdated(EMPTY_STRING))
                }

                WalletTopUpMainStore.Intent.AddCard -> scope.launch {
                    addCard(
                        cardNumber = getState().cardNumber,
                        validThru = getState().validThru,
                        cvc = getState().cvc,
                        name = getState().holderName,
                    )
                }

                is WalletTopUpMainStore.Intent.UpdateEmail -> dispatch(
                    Message.EmailUpdated(
                        intent.email, EmailValidator.invoke(intent.email)
                    )
                )

                is WalletTopUpMainStore.Intent.UpdateOxxoEmailBottomSheetVisibility -> dispatch(
                    Message.OxxoEmailBottomSheetVisibilityUpdated(intent.isVisible)
                )

                is WalletTopUpMainStore.Intent.UpdateOxxoAttentionBottomSheetVisibility -> dispatch(
                    Message.OxxoAttentionBottomSheetVisibilityUpdated(intent.isVisible)
                )

                is WalletTopUpMainStore.Intent.UpdateOxxoFullNameBottomSheetVisibility -> dispatch(
                    Message.OxxoFullNameBottomSheetVisibilityUpdated(intent.isVisible)
                )

                is WalletTopUpMainStore.Intent.UpdateOxxoFirstName -> dispatch(
                    Message.OxxoFirstNameUpdated(intent.name)
                )

                is WalletTopUpMainStore.Intent.UpdateOxxoLastName -> dispatch(
                    Message.OxxoLastNameUpdated(intent.name)
                )

                WalletTopUpMainStore.Intent.SubmitOxxoNames -> scope.launch {
                    updateUserNames(getState().firstName, getState().lastName)
                }

                WalletTopUpMainStore.Intent.SubmitEmail -> scope.launch {
                    updateUserEmail(getState().email)
                }
            }
        }

        private fun runActions() {
            scope.launch { getCards() }
            scope.launch { getHolderName() }
            scope.launch { getUser() }
        }

        private suspend fun getCards() {
            getCardsUseCase.invoke(
                onLoading = { dispatch(Message.SetCardsLoading) },
                onSuccess = { cards, defaultCardId ->
                    dispatch(Message.CardsLoaded(cards))
                    dispatch(Message.TopUpMethodUpdated(TopUpMethod.Card(defaultCardId)))
                },
                onError = { error ->
                    dispatch(Message.CardsFailed)
                    error.onError(
                        onGeneralError = { publish(WalletTopUpMainStore.Label.Error(it.message)) }
                    )
                }
            )
        }

        private suspend fun getUser() {
            userProfileRepository.getUserProfile(forceFetchFromRemote = false).collect {
                when (it) {
                    Result.Loading -> {}
                    is Result.Success -> {
                        val email = it.value.email

                        dispatch(
                            Message.UserUpdated(
                                firstName = it.value.firstName,
                                lastName = it.value.lastName,
                                email = email,
                            )
                        )

                        dispatch(
                            Message.EmailUpdated(
                                email = email,
                                isValid = EmailValidator.invoke(email)
                            )
                        )
                    }

                    is Result.Error -> {
                        when (val error = it.cause) {
                            is Error.General -> publish(WalletTopUpMainStore.Label.Error(error.message))
                            else -> {}
                        }
                    }
                }
            }
        }

        private suspend fun addCard(
            cardNumber: String,
            validThru: String,
            cvc: String,
            name: String,
        ) {
            addCardUseCase.invoke(
                cardNumber = cardNumber,
                validThru = validThru,
                cvc = cvc,
                name = name,
                onLoading = { dispatch(Message.SetAddCardLoading) },
                onSuccess = {
                    dispatch(Message.AddCardSuccess)
                    scope.launch { getCards() }
                },
                onError = {
                    it.onError(
                        onGeneralError = { error -> dispatch(Message.AddCardFailed(error.message)) }
                    )
                }
            )
        }

        private suspend fun getHolderName() {
            getHolderNameUseCase.invoke {
                dispatch(Message.HolderNameUpdated(it))
            }
        }

        private suspend fun topUp(
            amount: String,
            topUpMethod: TopUpMethod,
            firstName: String,
            lastName: String,
            email: String
        ) {
            topUpWalletUseCase.invoke(
                amount = amount,
                topUpMethod = topUpMethod,
                onLoading = { dispatch(Message.SetLoading) },
                onSuccess = { publish(WalletTopUpMainStore.Label.Success(amount)) },
                onOxxoPaymentSecretSuccess = { paymentSecret ->
                    publish(
                        WalletTopUpMainStore.Label.OxxoSuccess(
                            OxxoPayment(
                                firstName = firstName,
                                lastName = lastName,
                                email = email,
                                paymentSecret = paymentSecret,
                            )
                        )
                    )
                },
                onError = { error ->
                    dispatch(Message.SetError)
                    error.onError(
                        onGeneralError = { publish(WalletTopUpMainStore.Label.Error(it.message)) }
                    )
                }
            )
        }

        private suspend fun updateUserNames(firstName: String, lastName: String) {
            updateUserNamesUseCase.invoke(
                firstName = firstName,
                lastName = lastName,
                onLoading = { dispatch(Message.SetIsOxxoNamesLoading(true)) },
                onSuccess = { dispatch(Message.OxxoFullNameBottomSheetVisibilityUpdated(false)) },
                onError = {
                    dispatch(Message.SetIsOxxoNamesLoading(false))
                    it.onError(
                        onGeneralError = { error ->
                            publish(WalletTopUpMainStore.Label.Error(error.message))
                        }
                    )
                }
            )
        }

        private suspend fun updateUserEmail(email: String) {
            updateUserEmailUseCase.invoke(
                email = email,
                onLoading = { dispatch(Message.SetIsOxxoEmailLoading(true)) },
                onSuccess = { dispatch(Message.OxxoEmailBottomSheetVisibilityUpdated(false)) },
                onError = {
                    dispatch(Message.SetIsOxxoEmailLoading(false))
                    it.onError(
                        onGeneralError = { error ->
                            publish(WalletTopUpMainStore.Label.Error(error.message))
                        }
                    )
                }
            )
        }
    }

    private object ReducerImpl : Reducer<WalletTopUpMainStore.State, Message> {
        override fun WalletTopUpMainStore.State.reduce(msg: Message): WalletTopUpMainStore.State =
            when (msg) {
                Message.SetLoading -> copy(isLoading = true, isError = false)
                Message.SetError -> copy(isLoading = false, isError = true)
                is Message.UserUpdated -> copy(
                    firstName = msg.firstName,
                    lastName = msg.lastName,
                    email = msg.email
                )

                is Message.EmailUpdated -> copy(email = msg.email, isEmailValid = msg.isValid)
                is Message.OxxoEmailBottomSheetVisibilityUpdated -> copy(
                    isOxxoEmailBottomSheetVisible = msg.isVisible
                )

                is Message.SetIsOxxoEmailLoading -> copy(isOxxoEmailLoading = msg.isLoading)

                is Message.OxxoAttentionBottomSheetVisibilityUpdated -> copy(
                    isOxxoAttentionBottomSheetVisible = msg.isVisible
                )

                is Message.OxxoFullNameBottomSheetVisibilityUpdated -> copy(
                    isOxxoNamesBottomSheetVisible = msg.isVisible
                )

                is Message.OxxoFirstNameUpdated -> copy(firstName = msg.name)
                is Message.OxxoLastNameUpdated -> copy(lastName = msg.name)
                is Message.SetIsOxxoNamesLoading -> copy(isNamesLoading = msg.isLoading)

                is Message.TopUpMethodUpdated -> copy(selectedTopUpMethod = msg.topUpMethod)
                Message.SetCardsLoading -> copy(isCardsLoading = true, isError = false)
                is Message.CardsLoaded -> copy(
                    isCardsLoading = false,
                    isError = false,
                    cards = msg.cards
                )

                Message.CardsFailed -> copy(isCardsLoading = false, isError = true)
                is Message.AmountUpdated -> copy(amount = msg.amount)
                is Message.AddCardBottomSheetVisibilityUpdated -> copy(isAddCardBottomSheetVisible = msg.isVisible)
                is Message.CardNumberUpdated -> copy(cardNumber = msg.cardNumber)
                is Message.HolderNameUpdated -> copy(holderName = msg.name)
                is Message.ValidThruUpdated -> copy(validThru = msg.validThru)
                is Message.CvcUpdated -> copy(cvc = msg.cvc)
                Message.SetAddCardLoading -> copy(
                    isAddCardLoading = true,
                    isAddCardError = false,
                    addCardErrorMessage = EMPTY_STRING
                )

                Message.AddCardSuccess -> copy(
                    isAddCardLoading = false,
                    isAddCardError = false,
                    addCardErrorMessage = EMPTY_STRING,
                    isAddCardBottomSheetVisible = false,
                    holderName = EMPTY_STRING,
                    cardNumber = EMPTY_STRING,
                    validThru = EMPTY_STRING,
                    cvc = EMPTY_STRING,
                )

                is Message.AddCardFailed -> copy(
                    isAddCardLoading = false,
                    isAddCardError = true,
                    addCardErrorMessage = msg.message
                )
            }
    }

    private sealed interface Message {
        data object SetLoading : Message
        data object SetError : Message
        data class UserUpdated(val firstName: String, val lastName: String, val email: String) :
            Message

        data class TopUpMethodUpdated(val topUpMethod: TopUpMethod) : Message
        data class OxxoEmailBottomSheetVisibilityUpdated(val isVisible: Boolean) : Message
        data class OxxoAttentionBottomSheetVisibilityUpdated(val isVisible: Boolean) : Message
        data class EmailUpdated(val email: String, val isValid: Boolean) : Message
        data class SetIsOxxoEmailLoading(val isLoading: Boolean) : Message
        data class OxxoFullNameBottomSheetVisibilityUpdated(val isVisible: Boolean) : Message
        data class OxxoFirstNameUpdated(val name: String) : Message
        data class OxxoLastNameUpdated(val name: String) : Message
        data class SetIsOxxoNamesLoading(val isLoading: Boolean) : Message
        data object SetCardsLoading : Message
        data class CardsLoaded(val cards: List<Card>) : Message
        data object CardsFailed : Message
        data class AmountUpdated(val amount: String) : Message
        data class AddCardBottomSheetVisibilityUpdated(val isVisible: Boolean) : Message
        data class CardNumberUpdated(val cardNumber: String) : Message
        data class HolderNameUpdated(val name: String) : Message
        data class ValidThruUpdated(val validThru: String) : Message
        data class CvcUpdated(val cvc: String) : Message
        data object SetAddCardLoading : Message
        data object AddCardSuccess : Message
        data class AddCardFailed(val message: String) : Message
    }
}