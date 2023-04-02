/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store.di

import kotlin.random.Random
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import tech.antibytes.awesomecats.store.CatState
import tech.antibytes.awesomecats.store.UseCaseContract
import tech.antibytes.awesomecats.store.data.Repository
import tech.antibytes.awesomecats.store.data.RepositoryContract.HOST
import tech.antibytes.awesomecats.store.data.client.CatClient
import tech.antibytes.awesomecats.store.domain.RepositoryContract
import tech.antibytes.awesomecats.store.domain.UseCase
import tech.antibytes.pixabay.sdk.ClientContract
import tech.antibytes.wrapper.coroutine.wrapper.CoroutineWrapperContract.CoroutineScopeDispatcher
import tech.antibytes.wrapper.coroutine.wrapper.SharedFlowWrapper

private fun resolveCatStoreParameterModule(
    producerScope: CoroutineScopeDispatcher,
    logger: ClientContract.Logger,
    connection: ClientContract.ConnectivityManager,
    seed: Int,
    host: String,
): Module {
    return module {
        factory { producerScope }
        single { Random(seed) }
        single { CatClient.getInstance(logger, connection, host) }
    }
}

private fun resolveRepositories(): Module {
    return module {
        single<RepositoryContract> {
            Repository(get(), get())
        }
    }
}

private fun resolveUseCase(): Module {
    return module {
        single<UseCaseContract> {
            UseCase(get())
        }
    }
}

private fun resolveCatStoreModule(
    consumerScope: CoroutineScopeDispatcher,
): Module {
    return module {
        single<MutableStateFlow<CatState>> {
            MutableStateFlow(CatState.Initial)
        }
        single {
            SharedFlowWrapper.getInstance(
                get<MutableStateFlow<CatState>>(),
                consumerScope,
            )
        }
    }
}

internal fun initKoin(
    logger: ClientContract.Logger,
    connection: ClientContract.ConnectivityManager,
    seed: Int,
    producerScope: CoroutineScopeDispatcher,
    consumerScope: CoroutineScopeDispatcher,
    host: String = HOST,
): KoinApplication {
    return koinApplication {
        modules(
            resolveCatStoreParameterModule(
                producerScope,
                logger,
                connection,
                seed,
                host,
            ),
            resolveRepositories(),
            resolveUseCase(),
            resolveCatStoreModule(consumerScope),
        )
    }
}
