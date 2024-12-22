package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.repository

import com.bitcot.saludnow.client.core.network.platform.DispatchersProvider
import com.bitcot.saludnow.client.core.network.util.Result
import com.bitcot.saludnow.client.core.network.util.error
import com.bitcot.saludnow.client.core.network.util.flowCall
import com.bitcot.saludnow.client.core.network.util.success
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.mapper.toRequest
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.mapper.toResult
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.model.TopUpOxxoRequest
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.remote.WalletService
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.model.BalanceResult
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.model.Operation
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.model.TopUpOxxoResult
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.model.TopUpStripeWallet
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow

class WalletRepositoryImpl(
    private val dispatchers: DispatchersProvider,
    private val walletService: WalletService,
) : WalletRepository {
    override suspend fun getBalance(): Flow<Result<BalanceResult>> =
        flowCall(
            dispatcher = dispatchers.io,
            request = { walletService.getBalance() },
            response = {
                if (it.isSuccess) success(it.toResult())
                else error(it.message)
            }
        )

    override suspend fun getOperationsHistory(): Flow<Result<List<Operation>>> =
        flowCall(
            dispatcher = dispatchers.io,
            request = { walletService.getOperationsHistory() },
            response = {
                if (it.isSuccess) success(it.toResult())
                else error(it.message)
            }
        )

    override suspend fun topUpWithCard(data: TopUpStripeWallet): Flow<Result<Unit>> =
        flowCall(
            dispatcher = dispatchers.io,
            request = { walletService.topUpWithCard(data.toRequest()) },
            response = {
                if (it.isSuccess) success(Unit)
                else error(it.message)
            },
        )

    override suspend fun topUpWithOxxo(amount: Int): Flow<Result<TopUpOxxoResult>> =
        flowCall(
            dispatcher = dispatchers.io,
            request = { walletService.topUpWithOxxo(TopUpOxxoRequest(amount)) },
            response = {
                if (it.isSuccess) success(it.toResult())
                else error(it.message)
            }
        )
}