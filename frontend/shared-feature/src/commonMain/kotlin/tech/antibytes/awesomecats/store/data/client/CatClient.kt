/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store.data.client

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import kotlinx.serialization.json.Json
import tech.antibytes.awesomecats.store.data.RepositoryContract
import tech.antibytes.awesomecats.store.data.RepositoryContract.ENDPOINT
import tech.antibytes.awesomecats.store.data.RepositoryContract.HOST
import tech.antibytes.awesomecats.store.data.RepositoryContract.PORT
import tech.antibytes.awesomecats.store.data.client.networking.ClientConfigurator
import tech.antibytes.awesomecats.store.data.client.networking.HttpErrorMapper
import tech.antibytes.awesomecats.store.data.client.networking.NetworkingContract
import tech.antibytes.awesomecats.store.data.client.networking.RequestBuilder
import tech.antibytes.awesomecats.store.data.client.networking.plugin.LoggingConfigurator
import tech.antibytes.awesomecats.store.data.client.networking.plugin.ResponseValidatorConfigurator
import tech.antibytes.awesomecats.store.data.client.networking.plugin.SerializerConfigurator
import tech.antibytes.awesomecats.store.data.client.networking.receive
import tech.antibytes.awesomecats.store.data.client.serialization.JsonConfigurator
import tech.antibytes.pixabay.sdk.ClientContract

internal class CatClient constructor(
    private val requestBuilder: NetworkingContract.RequestBuilderFactory,
    private val connectivityManager: ClientContract.ConnectivityManager,
) : RepositoryContract.Client {
    private suspend fun guardTransaction(
        action: suspend () -> String,
    ): String {
        return if (!connectivityManager.hasConnection()) {
            "{}"
        } else {
            action()
        }
    }

    private suspend fun fetchCatFromApi(): String {
        return requestBuilder
            .create()
            .prepare(
                path = ENDPOINT,
            ).receive()
    }

    override suspend fun fetchCat(): String = guardTransaction {
        try {
            fetchCatFromApi()
        } catch (e: Throwable) {
            "{}"
        }
    }

    companion object : RepositoryContract.ClientFactory {
        private fun initPlugins(
            logger: ClientContract.Logger,
        ): Set<NetworkingContract.Plugin<in Any, in Any?>> {
            val jsonConfig = JsonConfigurator()
            Json { jsonConfig.configure(this) }

            @Suppress("UNCHECKED_CAST")
            return setOf(
                NetworkingContract.Plugin(
                    ContentNegotiation,
                    SerializerConfigurator(),
                    jsonConfig,
                ) as NetworkingContract.Plugin<in Any, in Any?>,
                NetworkingContract.Plugin(
                    Logging,
                    LoggingConfigurator(),
                    logger,
                ) as NetworkingContract.Plugin<in Any, in Any?>,
                NetworkingContract.Plugin(
                    HttpCallValidator,
                    ResponseValidatorConfigurator(),
                    HttpErrorMapper(),
                ) as NetworkingContract.Plugin<in Any, in Any?>,
            )
        }

        private fun initRequestBuilder(
            logger: ClientContract.Logger,
        ): NetworkingContract.RequestBuilderFactory {
            return RequestBuilder.Factory(
                client = HttpClient().config {
                    ClientConfigurator.configure(
                        this,
                        initPlugins(
                            logger,
                        ),
                    )
                },
                protocol = URLProtocol.HTTP,
                host = HOST,
                port = PORT.toIntOrNull(),
            )
        }

        override fun getInstance(
            logger: ClientContract.Logger,
            connection: ClientContract.ConnectivityManager,
        ): RepositoryContract.Client {
            return CatClient(
                requestBuilder = initRequestBuilder(
                    logger,
                ),
                connectivityManager = connection,
            )
        }
    }
}
