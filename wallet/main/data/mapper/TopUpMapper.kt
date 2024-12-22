package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.mapper

import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.model.TopUpOxxoResponse
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.model.TopUpStripeRequest
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.model.TopUpOxxoResult
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.model.TopUpStripeWallet

fun TopUpStripeWallet.toRequest(): TopUpStripeRequest =
    TopUpStripeRequest(
        stripeCardId = this.stripeCardId,
        amount = this.amount,
    )

fun TopUpOxxoResponse.toResult(): TopUpOxxoResult =
    TopUpOxxoResult(
        isSuccess = this.isSuccess,
        paymentSecret = this.paymentIntent?.paymentSecret.orEmpty(),
    )