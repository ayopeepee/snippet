package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.success.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.success.WalletTopUpSuccessComponent
import com.bitcot.saludnow.client.ui.button.SecondaryButton
import com.bitcot.saludnow.client.ui.icon.CheckOctagon
import com.bitcot.saludnow.client.ui.text.TitleText
import org.jetbrains.compose.resources.stringResource
import saludnow_patient.composeapp.generated.resources.Res
import saludnow_patient.composeapp.generated.resources.profile_wallet_top_up_success_amount
import saludnow_patient.composeapp.generated.resources.profile_wallet_top_up_success_go_back

@Composable
fun WalletTopUpSuccessContent(
    amount: String,
    onOutput: (WalletTopUpSuccessComponent.Output) -> Unit,
) {
    Scaffold(
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        imageVector = CheckOctagon,
                        contentDescription = null,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TitleText(
                        text = stringResource(
                            Res.string.profile_wallet_top_up_success_amount,
                            amount
                        ),
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        textAlign = TextAlign.Center,
                    )
                }

                SecondaryButton(
                    onClick = { onOutput(WalletTopUpSuccessComponent.Output.NavigateToProfile) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                ) {
                    Text(stringResource(Res.string.profile_wallet_top_up_success_go_back))
                }
            }
        }
    )
}