/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store.data

import tech.antibytes.awesomecats.common.CAT_HOST
import tech.antibytes.awesomecats.common.CAT_PORT
import tech.antibytes.pixabay.sdk.ClientContract

internal object RepositoryContract {

    interface Client {
        /**
         * Fetches an cat image
         * @return String with the serialized Cat
         */
        suspend fun fetchCat(): String
    }

    interface ClientFactory {
        /**
         * Builds an Client
         * @param logger - a logger implemented by you to keep track of things
         * @param connection - a barrier to not be sure the client is connected to the internet
         * @return Client
         */
        fun getInstance(
            logger: ClientContract.Logger,
            connection: ClientContract.ConnectivityManager,
            host: String = HOST,
        ): Client
    }

    internal val ENDPOINT = listOf("/")
    internal const val HOST = CAT_HOST
    internal const val PORT = CAT_PORT.toString()
}
