package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.main.store

import com.arkivanov.mvikotlin.core.store.Store
import com.bitcot.saludnow.client.feature.bottomnav.tab.home.appointment.confirm.domain.card.model.Card
import com.bitcot.saludnow.client.feature.bottomnav.tab.home.appointment.oxxo.domain.model.OxxoPayment
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.main.domain.model.TopUpMethod

interface WalletTopUpMainStore :
    Store<WalletTopUpMainStore.Intent, WalletTopUpMainStore.State, WalletTopUpMainStore.Label> {

    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val firstName: String = "",
        val lastName: String = "",
        val isNamesLoading: Boolean = false,
        val isOxxoNamesBottomSheetVisible: Boolean = false,
        val email: String = "",
        val selectedTopUpMethod: TopUpMethod = TopUpMethod.None,
        val isCardsLoading: Boolean = false,
        val cards: List<Card> = emptyList(),
        val amount: String = "",
        val isAddCardBottomSheetVisible: Boolean = false,
        val cardNumber: String = "",
        val holderName: String = "",
        val validThru: String = "",
        val cvc: String = "",
        val isAddCardLoading: Boolean = false,
        val isAddCardError: Boolean = false,
        val addCardErrorMessage: String = "",
        val isOxxoEmailBottomSheetVisible: Boolean = false,
        val isEmailValid: Boolean = false,
        val isOxxoEmailLoading: Boolean = false,
        val isOxxoAttentionBottomSheetVisible: Boolean = false,
    ) {
        val isTopUpButtonEnabled = when (selectedTopUpMethod) {
            is TopUpMethod.Card -> amount.isNotBlank() && selectedTopUpMethod.id.isNotBlank()
            TopUpMethod.Oxxo -> amount.isNotBlank()
            TopUpMethod.None -> false
        }

        val isAddCardButtonEnabled: Boolean =
            holderName.isNotBlank()
                    && cardNumber.length in listOf(14, 15, 16)
                    && validThru.length == 4
                    && cvc.length in listOf(3, 4)

        val isFullNameValid = firstName.length >= 2 && lastName.length >= 2
    }

    sealed interface Intent {
        data class TopUpMethodUpdated(val topUpMethod: TopUpMethod) : Intent
        data class UpdateAmount(val amount: String) : Intent
        data object TopUp : Intent
        data class UpdateAddCardBottomSheetVisibility(val isVisible: Boolean) : Intent
        data class UpdateHolderName(val holderName: String) : Intent
        data class UpdateCardNumber(val cardNumber: String) : Intent
        data class UpdateValidThru(val validThru: String) : Intent
        data class UpdateCvc(val cvc: String) : Intent
        data object ClearCardInputs : Intent
        data object AddCard : Intent
        data class UpdateOxxoEmailBottomSheetVisibility(val isVisible: Boolean) : Intent
        data class UpdateEmail(val email: String) : Intent
        data class UpdateOxxoAttentionBottomSheetVisibility(val isVisible: Boolean) : Intent
        data class UpdateOxxoFullNameBottomSheetVisibility(val isVisible: Boolean) : Intent
        data class UpdateOxxoFirstName(val name: String) : Intent
        data class UpdateOxxoLastName(val name: String) : Intent
        data object SubmitOxxoNames : Intent
        data object SubmitEmail : Intent
    }

    sealed interface Label {
        data class Success(val amount: String) : Label
        data class OxxoSuccess(val payment: OxxoPayment) : Label
        data class Error(val message: String) : Label
    }
}