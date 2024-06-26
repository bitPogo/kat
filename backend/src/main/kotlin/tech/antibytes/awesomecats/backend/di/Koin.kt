/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.backend.di

import kotlin.random.Random
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module
import tech.antibytes.awesomecats.backend.config.MainConfig
import tech.antibytes.awesomecats.backend.pixabay.data.datsource.DataSource
import tech.antibytes.awesomecats.backend.pixabay.data.repository.DataSourceContract
import tech.antibytes.awesomecats.backend.pixabay.data.repository.Repository
import tech.antibytes.awesomecats.backend.pixabay.domain.usecase.RepositoryContract
import tech.antibytes.awesomecats.backend.pixabay.domain.usecase.Usecase
import tech.antibytes.awesomecats.backend.pixabay.domain.usecase.UsecaseContract
import tech.antibytes.awesomecats.common.Logger
import tech.antibytes.pixabay.sdk.ClientContract
import tech.antibytes.pixabay.sdk.PixabayClient

fun composeApplication(): KoinApplication {
    return startKoin {
        modules(
            module {
                single {
                    Random(seed = MainConfig.seed)
                }

                single<ClientContract.Client> {
                    PixabayClient.getInstance(
                        MainConfig.apiKey,
                        Logger(::println, System.err::println),
                        { true },
                    )
                }

                single<DataSourceContract> {
                    DataSource(get(), get())
                }

                single<RepositoryContract> {
                    Repository(get(), get())
                }

                single<CoroutineDispatcher> {
                    Dispatchers.IO
                }

                single<UsecaseContract> {
                    Usecase(get(), get())
                }
            },
        )
    }
}
