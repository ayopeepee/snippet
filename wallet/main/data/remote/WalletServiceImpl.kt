package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.remote

import com.bitcot.saludnow.client.core.network.endPoint
import com.bitcot.saludnow.client.core.network.util.SimpleResponse
import com.bitcot.saludnow.client.core.network.util.tryRequest
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.model.BalanceResponse
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.model.OperationsHistoryResponse
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.model.TopUpOxxoRequest
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.model.TopUpOxxoResponse
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.model.TopUpStripeRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class WalletServiceImpl(
    private val httpClient: HttpClient,
) : WalletService {
    override suspend fun getBalance(): BalanceResponse =
        tryRequest {
            httpClient.get {
                endPoint(Path.GET_BALANCE)
            }
        }

    override suspend fun getOperationsHistory(): OperationsHistoryResponse =
        tryRequest {
            httpClient.get {
                endPoint(Path.GET_OPERATIONS)
            }
        }

    override suspend fun topUpWithCard(request: TopUpStripeRequest): SimpleResponse =
        tryRequest {
            httpClient.post {
                endPoint(Path.TOP_UP_STRIPE)
                setBody(request)
            }
        }

    override suspend fun topUpWithOxxo(request: TopUpOxxoRequest): TopUpOxxoResponse =
        tryRequest {
            httpClient.post {
                endPoint(Path.TOP_UP_OXXO)
                setBody(request)
            }
        }

    private companion object {
        object Path {
            const val GET_BALANCE = "client/cash/balance"
            const val GET_OPERATIONS = "client/cash/transactions"
            const val TOP_UP_STRIPE = "client/top-up/stripe"
            const val TOP_UP_OXXO = "client/top-up/oxxo"
        }
    }
}