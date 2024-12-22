package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bitcot.saludnow.client.core.constants.string.KEY_LAZY_COLUMN_BALANCE_BLOCK
import com.bitcot.saludnow.client.core.constants.string.WHATSAPP_CONTACT
import com.bitcot.saludnow.client.core.utils.Timestamp
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.WalletMainComponent
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.model.BundleType
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.model.CancellationInitiator
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.model.Operation
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.store.WalletMainStore
import com.bitcot.saludnow.client.ui.button.PrimaryButton
import com.bitcot.saludnow.client.ui.button.TopAppBarArrowNavigationButton
import com.bitcot.saludnow.client.ui.icon.ArrowUp
import com.bitcot.saludnow.client.ui.icon.ArrowUpOnSurface
import com.bitcot.saludnow.client.ui.icon.ClipboardHeartOnSurface
import com.bitcot.saludnow.client.ui.icon.HeartShineOnSurface
import com.bitcot.saludnow.client.ui.icon.Message
import com.bitcot.saludnow.client.ui.icon.Wallet
import com.bitcot.saludnow.client.ui.screen.NoInternetScreen
import com.bitcot.saludnow.client.ui.text.BodyText
import com.bitcot.saludnow.client.ui.text.TitleText
import com.valentinilk.shimmer.shimmer
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource
import saludnow_patient.composeapp.generated.resources.Res
import saludnow_patient.composeapp.generated.resources.profile_main_action_wallet_amount
import saludnow_patient.composeapp.generated.resources.profile_main_action_wallet_title
import saludnow_patient.composeapp.generated.resources.profile_wallet_main_balance_support
import saludnow_patient.composeapp.generated.resources.profile_wallet_main_balance_title
import saludnow_patient.composeapp.generated.resources.profile_wallet_main_balance_top_up
import saludnow_patient.composeapp.generated.resources.profile_wallet_main_no_operations
import saludnow_patient.composeapp.generated.resources.profile_wallet_main_operation_item_appointment_doctor
import saludnow_patient.composeapp.generated.resources.profile_wallet_main_operation_item_appointment_purchase
import saludnow_patient.composeapp.generated.resources.profile_wallet_main_operation_item_appointment_refund_cancel_by_doctor
import saludnow_patient.composeapp.generated.resources.profile_wallet_main_operation_item_appointment_refund_cancel_by_patient
import saludnow_patient.composeapp.generated.resources.profile_wallet_main_operation_item_bundle_lite
import saludnow_patient.composeapp.generated.resources.profile_wallet_main_operation_item_bundle_medium
import saludnow_patient.composeapp.generated.resources.profile_wallet_main_operation_item_bundle_premium
import saludnow_patient.composeapp.generated.resources.profile_wallet_main_operation_item_bundle_purchase
import saludnow_patient.composeapp.generated.resources.profile_wallet_main_operation_item_income_amount
import saludnow_patient.composeapp.generated.resources.profile_wallet_main_operation_item_outcome_amount
import saludnow_patient.composeapp.generated.resources.profile_wallet_main_operation_item_oxxo
import saludnow_patient.composeapp.generated.resources.profile_wallet_main_operation_item_promo_code
import saludnow_patient.composeapp.generated.resources.profile_wallet_main_operation_item_top_up
import saludnow_patient.composeapp.generated.resources.profile_wallet_main_top_bar_balance
import saludnow_patient.composeapp.generated.resources.today
import saludnow_patient.composeapp.generated.resources.yesterday

@Composable
fun WalletMainContent(
    state: WalletMainStore.State,
    onIntent: (WalletMainStore.Intent) -> Unit,
    onOutput: (WalletMainComponent.Output) -> Unit,
) {
    when {
        state.isNoInternet -> NoInternetScreen(onReloadClick = { onIntent(WalletMainStore.Intent.ReloadPage) })
        else -> Scaffold(
            topBar = {
                TopBar(
                    balance = state.balance,
                    showBalance = state.isBalanceBlockVisible.not(),
                    onNavigationIconClick = { onOutput(WalletMainComponent.Output.NavigateBack) },
                )
            },
            content = { paddingValues ->
                Body(
                    state = state,
                    onIntent = onIntent,
                    onOutput = onOutput,
                    paddingValues = paddingValues,
                )
            }
        )
    }
}

@Composable
fun TopBar(
    balance: String,
    showBalance: Boolean,
    onNavigationIconClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
            .statusBarsPadding()
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            TopAppBarArrowNavigationButton(onClick = onNavigationIconClick)

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TitleText(
                    text = stringResource(Res.string.profile_main_action_wallet_title),
                    fontSize = 16.sp,
                )

                AnimatedVisibility(showBalance) {
                    Spacer(modifier = Modifier.height(2.dp))

                    BodyText(
                        text = stringResource(
                            Res.string.profile_wallet_main_top_bar_balance,
                            balance
                        ),
                    )
                }
            }
        }
    }
}

@Composable
fun Body(
    state: WalletMainStore.State,
    onIntent: (WalletMainStore.Intent) -> Unit,
    onOutput: (WalletMainComponent.Output) -> Unit,
    paddingValues: PaddingValues,
) {
    val listState = rememberLazyListState()

    val isBalanceBlockVisible by remember {
        derivedStateOf {
            listState.layoutInfo.visibleItemsInfo.any { it.key == KEY_LAZY_COLUMN_BALANCE_BLOCK }
        }
    }

    LaunchedEffect(isBalanceBlockVisible) {
        onIntent(WalletMainStore.Intent.UpdateBalanceBlockVisibility(isBalanceBlockVisible))
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item { Spacer(modifier = Modifier.height(16.dp)) }

        item(key = KEY_LAZY_COLUMN_BALANCE_BLOCK) {
            BalanceBlock(
                balance = state.balance,
                balanceLoading = state.isBalanceLoading,
                onTopUpClick = { onOutput(WalletMainComponent.Output.NavigateToTopUp) },
                onSupportClick = { onIntent(WalletMainStore.Intent.OpenWhatsApp(WHATSAPP_CONTACT)) },
            )
        }

        if (state.operations.isNotEmpty()) {
            item { Spacer(modifier = Modifier.height(24.dp)) }

            OperationsBlock(
                operations = state.operations,
                onAppointmentOperationClick = {
                    onOutput(WalletMainComponent.Output.NavigateToAppointmentDetails(it))
                },
            )

            item {
                Spacer(modifier = Modifier.height(256.dp))

                TopUpButton(
                    onClick = { onOutput(WalletMainComponent.Output.NavigateToTopUp) },
                    modifier = Modifier.fillMaxWidth(),
                    isCardTopUpButton = false
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        } else {
            if (state.isOperationsLoading) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    OperationsShimmer()
                }
            } else {
                item {
                    Spacer(modifier = Modifier.height(128.dp))

                    NoOperationsBlock(
                        onBuyBundleClick = { onOutput(WalletMainComponent.Output.NavigateToTopUp) },
                    )
                }
            }
        }
    }
}

@Composable
fun BalanceBlock(
    balance: String,
    balanceLoading: Boolean,
    onTopUpClick: () -> Unit,
    onSupportClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .background(
                brush = Brush.linearGradient(listOf(Color(0xFFC02954), Color(0xFFCE5174))),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(top = 24.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            BodyText(
                text = stringResource(Res.string.profile_wallet_main_balance_title),
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (balanceLoading) {
                Box(
                    modifier = Modifier
                        .shimmer()
                        .background(
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = RoundedCornerShape(12.dp),
                        )
                        .size(width = 128.dp, height = 36.dp)
                )
            } else {
                TitleText(
                    text = stringResource(Res.string.profile_main_action_wallet_amount, balance),
                    fontSize = 36.sp,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row {
                TopUpButton(
                    onClick = onTopUpClick,
                    modifier = Modifier.weight(1f),
                    isCardTopUpButton = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                SupportButton(
                    onClick = onSupportClick,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

fun LazyListScope.OperationsBlock(
    operations: List<Pair<LocalDate, List<Operation>>>,
    onAppointmentOperationClick: (appointmentId: Long) -> Unit,
) {
    itemsIndexed(items = operations) { index, item ->
        val operationTimestamp = item.second.first().timestamp

        OperationsBlockItem(
            date = when {
                operationTimestamp.isToday() -> stringResource(Res.string.today)
                operationTimestamp.isYesterday() -> stringResource(Res.string.yesterday)
                else -> operationTimestamp.toMonthDay()
            },
            operations = item.second,
            onAppointmentOperationClick = onAppointmentOperationClick,
        )

        if (index < operations.lastIndex) {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun OperationsBlockItem(
    date: String,
    operations: List<Operation>,
    onAppointmentOperationClick: (appointmentId: Long) -> Unit,
) {
    Column {
        TitleText(
            text = date,
            fontSize = 16.sp,
        )

        Spacer(modifier = Modifier.height(16.dp))

        operations.forEachIndexed { index, item ->
            when (item) {
                is Operation.TopUp.BankCard -> BalanceCardTopUpOperationItem(item)
                is Operation.TopUp.Oxxo -> BalanceOxxoTopUpOperationItem(item)
                is Operation.TopUp.PromoCode -> BalancePromoCodeTopUpOperationItem(item)
                is Operation.AppointmentCancellationRefund -> AppointmentCancellationRefundOperationItem(
                    item
                ) {
                    onAppointmentOperationClick(item.appointmentId)
                }

                is Operation.AppointmentPurchase -> AppointmentPurchaseOperationItem(item) {
                    onAppointmentOperationClick(item.appointmentId)
                }

                is Operation.BundlePurchase -> BundlePurchaseOperationItem(item)
                is Operation.Unknown -> {}
            }

            if (index < operations.lastIndex) {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun NoOperationsBlock(
    onBuyBundleClick: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        BodyText(
            text = stringResource(Res.string.profile_wallet_main_no_operations),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onBuyBundleClick,
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0x0DE6E6E6),
                contentColor = MaterialTheme.colorScheme.onBackground,
            )
        ) {
            TitleText(
                text = stringResource(Res.string.profile_wallet_main_balance_top_up),
                fontSize = 16.sp,
            )

        }
    }
}

@Composable
fun TopUpButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isCardTopUpButton: Boolean
) {
    val buttonColors = if (isCardTopUpButton) {
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onBackground,
            contentColor = MaterialTheme.colorScheme.background,
        )
    } else {
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    }
    PrimaryButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
        colors = buttonColors
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = ArrowUp,
                contentDescription = null,
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(Res.string.profile_wallet_main_balance_top_up),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(600),
                )
            )
        }
    }
}

@Composable
fun SupportButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0x0DE6E6E6), // FIXME: hardcoded
            contentColor = MaterialTheme.colorScheme.onBackground,
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Message,
                contentDescription = null,
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(Res.string.profile_wallet_main_balance_support),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(600),
                )
            )
        }
    }
}

@Composable
fun BalanceCardTopUpOperationItem(
    topUp: Operation.TopUp.BankCard,
) {
    IncomeOperationItem(
        imageVector = ArrowUpOnSurface,
        title = stringResource(Res.string.profile_wallet_main_operation_item_top_up),
        body = "${topUp.brand.value} *${topUp.lastFourDigits}",
        income = topUp.amount,
        timestamp = topUp.timestamp,
        onClick = {},
    )
}

@Composable
fun BalanceOxxoTopUpOperationItem(
    topUp: Operation.TopUp.Oxxo,
) {
    IncomeOperationItem(
        imageVector = ArrowUpOnSurface,
        title = stringResource(Res.string.profile_wallet_main_operation_item_top_up),
        body = stringResource(Res.string.profile_wallet_main_operation_item_oxxo),
        income = topUp.amount,
        timestamp = topUp.timestamp,
        onClick = {},
    )
}

@Composable
fun BalancePromoCodeTopUpOperationItem(
    topUp: Operation.TopUp.PromoCode,
) {
    IncomeOperationItem(
        imageVector = ArrowUpOnSurface,
        title = stringResource(Res.string.profile_wallet_main_operation_item_top_up),
        body = stringResource(Res.string.profile_wallet_main_operation_item_promo_code),
        income = topUp.amount,
        timestamp = topUp.timestamp,
        onClick = {},
    )
}

@Composable
fun AppointmentCancellationRefundOperationItem(
    appointmentCancellationRefund: Operation.AppointmentCancellationRefund,
    onClick: () -> Unit,
) {
    IncomeOperationItem(
        imageVector = Wallet,
        title = when (appointmentCancellationRefund.initiator) {
            CancellationInitiator.DOCTOR -> stringResource(Res.string.profile_wallet_main_operation_item_appointment_refund_cancel_by_doctor)
            CancellationInitiator.PATIENT -> stringResource(Res.string.profile_wallet_main_operation_item_appointment_refund_cancel_by_patient)
        },
        body = stringResource(
            Res.string.profile_wallet_main_operation_item_appointment_doctor,
            appointmentCancellationRefund.doctorFullName,
        ),
        income = appointmentCancellationRefund.amount,
        timestamp = appointmentCancellationRefund.timestamp,
        onClick = onClick,
    )
}

@Composable
fun BundlePurchaseOperationItem(
    bundlePurchase: Operation.BundlePurchase,
) {
    IncomeOperationItem(
        imageVector = HeartShineOnSurface,
        title = stringResource(Res.string.profile_wallet_main_operation_item_bundle_purchase),
        body = when (bundlePurchase.type) {
            BundleType.LITE -> stringResource(Res.string.profile_wallet_main_operation_item_bundle_lite)
            BundleType.MEDIUM -> stringResource(Res.string.profile_wallet_main_operation_item_bundle_medium)
            BundleType.PREMIUM -> stringResource(Res.string.profile_wallet_main_operation_item_bundle_premium)
        },
        income = bundlePurchase.amount,
        timestamp = bundlePurchase.timestamp,
        onClick = {},
    )
}

@Composable
fun AppointmentPurchaseOperationItem(
    appointmentPurchase: Operation.AppointmentPurchase,
    onClick: () -> Unit,
) {
    OutcomeOperationItem(
        imageVector = ClipboardHeartOnSurface,
        title = stringResource(Res.string.profile_wallet_main_operation_item_appointment_purchase),
        body = stringResource(
            Res.string.profile_wallet_main_operation_item_appointment_doctor,
            appointmentPurchase.doctorFullName,
        ),
        outcome = appointmentPurchase.amount,
        timestamp = appointmentPurchase.timestamp,
        onClick = onClick,
    )
}

@Composable
fun IncomeOperationItem(
    imageVector: ImageVector,
    title: String,
    body: String,
    income: Int,
    timestamp: Timestamp,
    onClick: () -> Unit,
) {
    OperationItem(
        imageVector = imageVector,
        title = title,
        body = body,
        timestamp = timestamp,
        amount = {
            BodyText(
                text = stringResource(
                    Res.string.profile_wallet_main_operation_item_income_amount,
                    income,
                ),
                color = Color(0xFF27B446), // FIXME: hardcoded
            )
        },
        onClick = onClick,
    )
}

@Composable
fun OutcomeOperationItem(
    imageVector: ImageVector,
    title: String,
    body: String,
    outcome: Int,
    timestamp: Timestamp,
    onClick: () -> Unit,
) {
    OperationItem(
        imageVector = imageVector,
        title = title,
        body = body,
        timestamp = timestamp,
        amount = {
            BodyText(
                text = stringResource(
                    Res.string.profile_wallet_main_operation_item_outcome_amount,
                    outcome,
                ),
                color = MaterialTheme.colorScheme.onBackground,
            )
        },
        onClick = onClick,
    )
}

@Composable
fun OperationItem(
    imageVector: ImageVector,
    title: String,
    body: String,
    timestamp: Timestamp,
    amount: @Composable (ColumnScope.() -> Unit),
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier.clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OperationItemIcon(
            imageVector = imageVector,
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            BodyText(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.height(2.dp))

            BodyText(
                text = body,
                fontSize = 12.sp,
            )
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End,
        ) {
            amount()

            Spacer(modifier = Modifier.height(2.dp))

            BodyText(
                text = timestamp.toDayMonthNumberYearTime(),
                fontSize = 12.sp,
            )
        }
    }
}

@Composable
fun OperationItemIcon(
    imageVector: ImageVector,
) {
    Box(
        modifier = Modifier
            .background(
                color = Color(0x0DE6E6E6), // FIXME: hardcoded
                shape = CircleShape,
            )
    ) {
        Image(
            imageVector = imageVector,
            contentDescription = null,
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Composable
fun OperationsShimmer() {
    Column(
        modifier = Modifier
            .shimmer()
            .fillMaxWidth()
    ) {
        repeat(10) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0x0DE6E6E6), // FIXME: hardcoded
                            shape = CircleShape,
                        )
                        .size(44.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.onBackground,
                                shape = MaterialTheme.shapes.medium,
                            )
                            .size(width = 128.dp, height = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                shape = MaterialTheme.shapes.medium,
                            )
                            .size(width = 80.dp, height = 16.dp)
                    )
                }

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End,
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.onBackground,
                                shape = MaterialTheme.shapes.medium,
                            )
                            .size(width = 128.dp, height = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                shape = MaterialTheme.shapes.medium,
                            )
                            .size(width = 80.dp, height = 12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}