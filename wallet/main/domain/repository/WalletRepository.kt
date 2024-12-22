package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.repository

import com.bitcot.saludnow.client.core.network.util.Result
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.model.BalanceResult
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.model.Operation
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.model.TopUpOxxoResult
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.model.TopUpStripeWallet
import kotlinx.coroutines.flow.Flow

interface WalletRepository {

    suspend fun getBalance(): Flow<Result<BalanceResult>>

    suspend fun getOperationsHistory(): Flow<Result<List<Operation>>>

    suspend fun topUpWithCard(data: TopUpStripeWallet): Flow<Result<Unit>>

    suspend fun topUpWithOxxo(amount: Int): Flow<Result<TopUpOxxoResult>>
}