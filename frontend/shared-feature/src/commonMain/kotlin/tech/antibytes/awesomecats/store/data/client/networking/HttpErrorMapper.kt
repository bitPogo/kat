/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store.data.client.networking

import io.ktor.client.plugins.ResponseException
import tech.antibytes.awesomecats.store.data.client.error.CatClientError
import tech.antibytes.awesomecats.store.data.client.networking.plugin.KtorPluginsContract

internal class HttpErrorMapper : KtorPluginsContract.ErrorMapper {
    private fun wrapError(error: Throwable): Throwable {
        return if (error is ResponseException) {
            CatClientError.RequestError(error.response.status.value)
        } else {
            error
        }
    }

    override fun mapAndThrow(error: Throwable) {
        throw wrapError(error)
    }
}
