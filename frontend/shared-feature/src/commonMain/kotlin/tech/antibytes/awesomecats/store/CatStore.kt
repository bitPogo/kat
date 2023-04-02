/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.KoinApplication
import tech.antibytes.awesomecats.store.di.initKoin
import tech.antibytes.pixabay.sdk.ClientContract
import tech.antibytes.wrapper.coroutine.wrapper.CoroutineWrapperContract

class CatStore internal constructor(
    koin: KoinApplication,
) : CatStoreContract {
    override val catState: CoroutineWrapperContract.SharedFlowWrapper<CatState> by koin.koin.inject()
    private val catPropagator: MutableStateFlow<CatState> by koin.koin.inject()
    private val dispatcher: CoroutineWrapperContract.CoroutineScopeDispatcher by koin.koin.inject()
    private val catsUsecase: UseCaseContract by koin.koin.inject()

    private fun <T> executeEvent(
        propagator: MutableStateFlow<T>,
        event: suspend () -> T,
    ) {
        dispatcher.dispatch().launch {
            propagator.update { event() }
        }
    }

    private fun goIntoPendingState() {
        catPropagator.update { CatState.Pending }
    }

    private suspend fun resolvePlainCat(): CatState {
        val cat = catsUsecase.findACat()

        return if (cat.url.isEmpty()) {
            CatState.Error(RuntimeException("Something did go wrong."))
        } else {
            CatState.Accepted(cat)
        }
    }

    override fun requestACat() {
        goIntoPendingState()

        executeEvent(catPropagator) {
            resolvePlainCat()
        }
    }

    companion object : CatStoreFactoryContract {
        override fun getInstance(
            logger: ClientContract.Logger,
            connection: ClientContract.ConnectivityManager,
            seed: Int,
            producerScope: CoroutineWrapperContract.CoroutineScopeDispatcher,
            consumerScope: CoroutineWrapperContract.CoroutineScopeDispatcher,
            host: String,
        ): CatStoreContract {
            return CatStore(
                initKoin(
                    logger,
                    connection,
                    seed,
                    producerScope,
                    consumerScope,
                    host,
                ),
            )
        }
    }
}
