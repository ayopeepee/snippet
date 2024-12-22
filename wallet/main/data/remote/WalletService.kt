package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.remote

import com.bitcot.saludnow.client.core.network.util.SimpleResponse
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.model.BalanceResponse
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.model.OperationsHistoryResponse
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.model.TopUpOxxoRequest
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.model.TopUpOxxoResponse
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.model.TopUpStripeRequest

interface WalletService {

    suspend fun getBalance(): BalanceResponse

    suspend fun getOperationsHistory(): OperationsHistoryResponse

    suspend fun topUpWithCard(request: TopUpStripeRequest): SimpleResponse

    suspend fun topUpWithOxxo(request: TopUpOxxoRequest): TopUpOxxoResponse
}