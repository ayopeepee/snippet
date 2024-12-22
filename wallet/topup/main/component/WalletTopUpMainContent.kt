package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.main.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bitcot.saludnow.client.core.constants.string.EMPTY_STRING
import com.bitcot.saludnow.client.feature.bottomnav.tab.home.appointment.confirm.component.AddCardBottomSheet
import com.bitcot.saludnow.client.feature.bottomnav.tab.home.appointment.confirm.component.OxxoEmailBottomSheet
import com.bitcot.saludnow.client.feature.bottomnav.tab.home.appointment.confirm.component.OxxoNamesBottomSheet
import com.bitcot.saludnow.client.feature.bottomnav.tab.home.appointment.confirm.component.logo
import com.bitcot.saludnow.client.feature.bottomnav.tab.home.appointment.confirm.domain.card.model.Card
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.main.WalletTopUpMainComponent
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.main.domain.model.TopUpMethod
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.main.store.WalletTopUpMainStore
import com.bitcot.saludnow.client.ui.bottomsheet.InfoBottomSheet
import com.bitcot.saludnow.client.ui.button.PrimaryButton
import com.bitcot.saludnow.client.ui.icon.Card
import com.bitcot.saludnow.client.ui.icon.Oxxo
import com.bitcot.saludnow.client.ui.progressindicator.BaseFullScreenCircleLoader
import com.bitcot.saludnow.client.ui.snackbar.MessageSnackbar
import com.bitcot.saludnow.client.ui.text.BodyText
import com.bitcot.saludnow.client.ui.text.TitleText
import com.bitcot.saludnow.client.ui.textfield.LimitedTextField
import com.bitcot.saludnow.client.ui.topappbar.BaseTopAppBar
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource
import saludnow_patient.composeapp.generated.resources.Res
import saludnow_patient.composeapp.generated.resources.attention
import saludnow_patient.composeapp.generated.resources.okay_understand
import saludnow_patient.composeapp.generated.resources.profile_wallet_top_up_add_card
import saludnow_patient.composeapp.generated.resources.profile_wallet_top_up_amount
import saludnow_patient.composeapp.generated.resources.profile_wallet_top_up_card_item_title
import saludnow_patient.composeapp.generated.resources.profile_wallet_top_up_oxxo_body
import saludnow_patient.composeapp.generated.resources.profile_wallet_top_up_oxxo_pay
import saludnow_patient.composeapp.generated.resources.profile_wallet_top_up_payment_methods
import saludnow_patient.composeapp.generated.resources.profile_wallet_top_up_submit
import saludnow_patient.composeapp.generated.resources.profile_wallet_top_up_title

@Composable
fun WalletTopUpMainContent(
    state: WalletTopUpMainStore.State,
    label: Flow<WalletTopUpMainStore.Label>,
    onIntent: (WalletTopUpMainStore.Intent) -> Unit,
    onOutput: (WalletTopUpMainComponent.Output) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    if (state.isLoading) {
        BaseFullScreenCircleLoader()
    } else {
        Scaffold(
            topBar = {
                BaseTopAppBar(
                    title = stringResource(Res.string.profile_wallet_top_up_title),
                    onNavigationIconClick = { onOutput(WalletTopUpMainComponent.Output.NavigateBack) },
                )
            },
            content = { paddingValues ->
                Body(
                    state = state,
                    onIntent = onIntent,
                    paddingValues = paddingValues,
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState, snackbar = { data ->
                    MessageSnackbar(data)
                })
            }
        )
    }

    LaunchedEffect(label) {
        label.collectLatest {
            when (it) {
                is WalletTopUpMainStore.Label.Success -> onOutput(
                    WalletTopUpMainComponent.Output.NavigateToTopUpSuccess(
                        it.amount
                    )
                )

                is WalletTopUpMainStore.Label.OxxoSuccess -> onOutput(
                    WalletTopUpMainComponent.Output.NavigateToOxxo(it.payment)
                )

                is WalletTopUpMainStore.Label.Error -> snackbarHostState.showSnackbar(it.message)
            }
        }
    }

    when {
        state.isAddCardBottomSheetVisible -> AddCardBottomSheet(
            cardNumber = state.cardNumber,
            validThru = state.validThru,
            cvc = state.cvc,
            holderName = state.holderName,
            isAddCardError = state.isAddCardError,
            addCardErrorMessage = state.addCardErrorMessage,
            isAddCardButtonEnabled = state.isAddCardButtonEnabled,
            isAddCardLoading = state.isAddCardLoading,
            onCardNumberChange = { onIntent(WalletTopUpMainStore.Intent.UpdateCardNumber(it)) },
            onHolderNameChange = { onIntent(WalletTopUpMainStore.Intent.UpdateHolderName(it)) },
            onValidThruChange = { onIntent(WalletTopUpMainStore.Intent.UpdateValidThru(it)) },
            onCvcChange = { onIntent(WalletTopUpMainStore.Intent.UpdateCvc(it)) },
            onAddCardClick = { onIntent(WalletTopUpMainStore.Intent.AddCard) },
            onDismiss = {
                onIntent(WalletTopUpMainStore.Intent.UpdateAddCardBottomSheetVisibility(false))
                onIntent(WalletTopUpMainStore.Intent.ClearCardInputs)
            }
        )

        state.isOxxoEmailBottomSheetVisible -> OxxoEmailBottomSheet(
            email = state.email,
            valid = state.isEmailValid,
            loading = state.isOxxoEmailLoading,
            onEmailChange = { onIntent(WalletTopUpMainStore.Intent.UpdateEmail(it)) },
            onConfirm = { onIntent(WalletTopUpMainStore.Intent.SubmitEmail) },
            onDismiss = {
                onIntent(
                    WalletTopUpMainStore.Intent.UpdateOxxoEmailBottomSheetVisibility(
                        false
                    )
                )
            }
        )

        state.isOxxoAttentionBottomSheetVisible -> OxxoTopUpAttentionBottomSheet(
            onDismiss = {
                onOutput(WalletTopUpMainComponent.Output.NavigateBack)
            },
            onConfirm = {
                onOutput(WalletTopUpMainComponent.Output.NavigateBack)
            }
        )

        state.isOxxoNamesBottomSheetVisible -> OxxoNamesBottomSheet(
            firstName = state.firstName,
            lastName = state.lastName,
            valid = state.isFullNameValid,
            loading = state.isNamesLoading,
            onFirstNameChange = { onIntent(WalletTopUpMainStore.Intent.UpdateOxxoFirstName(it)) },
            onLastNameChange = { onIntent(WalletTopUpMainStore.Intent.UpdateOxxoLastName(it)) },
            onConfirm = { onIntent(WalletTopUpMainStore.Intent.SubmitOxxoNames) },
            onDismiss = {
                onIntent(WalletTopUpMainStore.Intent.UpdateOxxoFirstName(EMPTY_STRING))
                onIntent(WalletTopUpMainStore.Intent.UpdateOxxoLastName(EMPTY_STRING))

                onIntent(
                    WalletTopUpMainStore.Intent.UpdateOxxoFullNameBottomSheetVisibility(
                        isVisible = false
                    )
                )
            }
        )
    }
}

@Composable
fun Body(
    state: WalletTopUpMainStore.State,
    onIntent: (WalletTopUpMainStore.Intent) -> Unit,
    paddingValues: PaddingValues,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(paddingValues)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        PaymentMethodsBlock(
            selectedTopUpMethod = state.selectedTopUpMethod,
            cardsLoading = state.isCardsLoading,
            cards = state.cards,
            onTopUpMethodClick = { onIntent(WalletTopUpMainStore.Intent.TopUpMethodUpdated(it)) },
            onAddCardClick = {
                onIntent(
                    WalletTopUpMainStore.Intent.UpdateAddCardBottomSheetVisibility(
                        true
                    )
                )
            },
        )

        Spacer(modifier = Modifier.height(16.dp))

        LimitedTextField(
            value = state.amount,
            onValueChange = { onIntent(WalletTopUpMainStore.Intent.UpdateAmount(it)) },
            label = { Text(stringResource(Res.string.profile_wallet_top_up_amount)) },
            singleLine = true,
            keyboardType = KeyboardType.Number,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(16.dp))

        TopUpButton(
            enabled = state.isTopUpButtonEnabled,
            onClick = { onIntent(WalletTopUpMainStore.Intent.TopUp) },
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun PaymentMethodsBlock(
    selectedTopUpMethod: TopUpMethod,
    cardsLoading: Boolean,
    cards: List<Card>,
    onTopUpMethodClick: (TopUpMethod) -> Unit,
    onAddCardClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp)
        ) {
            TitleText(
                text = stringResource(Res.string.profile_wallet_top_up_payment_methods),
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 16.sp,
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow {
                item { Spacer(modifier = Modifier.width(16.dp)) }

                item {
                    PaymentMethodItem(
                        title = stringResource(Res.string.profile_wallet_top_up_oxxo_pay),
                        logo = Oxxo,
                        selected = selectedTopUpMethod is TopUpMethod.Oxxo,
                        onClick = { onTopUpMethodClick(TopUpMethod.Oxxo) },
                    )
                }

                if (cards.isNotEmpty()) {
                    item { Spacer(modifier = Modifier.width(8.dp)) }
                }

                if (cardsLoading) {
                    item {
                        Spacer(modifier = Modifier.width(8.dp))
                        PaymentMethodItemShimmer()
                    }
                } else {
                    itemsIndexed(items = cards) { index, item ->
                        PaymentMethodItem(
                            title = stringResource(
                                Res.string.profile_wallet_top_up_card_item_title,
                                item.lastFourDigits
                            ),
                            logo = item.brand.logo(),
                            selected = selectedTopUpMethod is TopUpMethod.Card && item.id == selectedTopUpMethod.id,
                            onClick = { onTopUpMethodClick(TopUpMethod.Card(item.id)) },
                        )

                        if (index < cards.lastIndex) {
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.width(8.dp))

                    AddCardItem(
                        onClick = onAddCardClick,
                    )

                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }
    }
}

@Composable
fun PaymentMethodItem(
    title: String,
    logo: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .border(
                width = 1.dp,
                color = if (selected) {
                    MaterialTheme.colorScheme.onBackground
                } else {
                    MaterialTheme.colorScheme.secondaryContainer
                },
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(12.dp),
            )
    ) {
        Box(
            modifier = Modifier
                .padding(4.dp)
                .sizeIn(minWidth = 128.dp, minHeight = 92.dp)
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            TitleText(
                text = title,
                fontSize = 12.sp,
                modifier = Modifier.padding(12.dp)
            )

            Image(
                imageVector = logo,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            )
        }
    }
}

@Composable
fun AddCardItem(
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(12.dp),
            )
    ) {
        Box(
            modifier = Modifier
                .padding(4.dp)
                .sizeIn(minWidth = 128.dp, minHeight = 92.dp)
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = Card,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(modifier = Modifier.height(8.dp))

                BodyText(
                    text = stringResource(Res.string.profile_wallet_top_up_add_card),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun TopUpButton(
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        PrimaryButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 16.dp,
                    spotColor = Color(0x5227AE60), // FIXME: hardcoded
                ),
            shape = RoundedCornerShape(90.dp),
            enabled = enabled,
        ) {
            TitleText(
                text = stringResource(Res.string.profile_wallet_top_up_submit),
                fontSize = 16.sp,
                lineHeight = 16.sp,
            )
        }
    }
}

@Composable
fun OxxoTopUpAttentionBottomSheet(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    InfoBottomSheet(
        title = stringResource(Res.string.attention),
        body = stringResource(Res.string.profile_wallet_top_up_oxxo_body),
        buttonText = stringResource(Res.string.okay_understand),
        onDismiss = onDismiss,
        onConfirm = onConfirm,
    )
}

@Composable
fun PaymentMethodItemShimmer() {
    Box(
        modifier = Modifier
            .shimmer()
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(12.dp)
            )
            .size(width = 136.dp, height = 100.dp)
    )
}