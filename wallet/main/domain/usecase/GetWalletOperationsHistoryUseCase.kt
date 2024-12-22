package com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.usecase

import com.bitcot.saludnow.client.core.network.platform.DispatchersProvider
import com.bitcot.saludnow.client.core.network.util.Result
import com.bitcot.saludnow.client.core.network.util.RootError
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.model.Operation
import com.bitcot.saludnow.client.feature.bottomnav.tab.profile.wallet.main.domain.repository.WalletRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.datetime.LocalDate

class GetWalletOperationsHistoryUseCase(
    private val dispatchers: DispatchersProvider,
    private val walletRepository: WalletRepository,
) {
    suspend operator fun invoke(
        scope: CoroutineScope,
        onLoading: () -> Unit,
        onSuccess: (operations: List<Pair<LocalDate, List<Operation>>>) -> Unit,
        onError: (RootError) -> Unit,
    ) {
        walletRepository.getOperationsHistory().collect {
            when (it) {
                Result.Loading -> onLoading()
                is Result.Success -> {
                    val groupedOperations = scope.async(dispatchers.default) {
                        it.value.groupBy { operation -> operation.timestamp.date() }
                    }

                    onSuccess(groupedOperations.await().toList())
                }

                is Result.Error -> onError(it.cause)
            }
        }
    }
}