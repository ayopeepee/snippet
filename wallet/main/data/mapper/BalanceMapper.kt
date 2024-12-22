package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.mapper

import com.bitcot.saludnow.client.core.extensions.orZero
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.model.BalanceResponse
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.model.BalanceResult

fun BalanceResponse.toResult(): BalanceResult =
    BalanceResult(
        isSuccess = this.isSuccess,
        balance = this.balance.orZero(),
    )