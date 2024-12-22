package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.di

import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.remote.WalletService
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.remote.WalletServiceImpl
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.data.repository.WalletRepositoryImpl
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.repository.WalletRepository
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.usecase.GetWalletBalanceUseCase
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.usecase.GetWalletOperationsHistoryUseCase
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.topup.main.domain.usecase.TopUpWalletUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

val walletModule: Module = module {
    single<WalletService> { WalletServiceImpl(httpClient = get()) }
    single<WalletRepository> { WalletRepositoryImpl(dispatchers = get(), walletService = get()) }
    factory { TopUpWalletUseCase(walletRepository = get()) }
    factory { GetWalletBalanceUseCase(walletRepository = get()) }
    factory { GetWalletOperationsHistoryUseCase(dispatchers = get(), walletRepository = get()) }
}