/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store.data.client.error

sealed class CatClientError(
    message: String? = null,
    cause: Throwable? = null,
) : Error(message, cause) {
    class NoConnection : CatClientError()
    class RequestError(val status: Int) : CatClientError()
    class RequestValidationFailure(message: String) : CatClientError(message)
    class ResponseTransformFailure : CatClientError(message = "Unexpected Response")
}
