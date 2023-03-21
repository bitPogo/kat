/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store.data.client.networking

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.host
import io.ktor.client.request.parameter
import io.ktor.client.request.port
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpStatement
import io.ktor.http.HttpMethod
import io.ktor.http.URLProtocol
import io.ktor.http.path
import tech.antibytes.awesomecats.store.data.client.error.CatClientError
import tech.antibytes.awesomecats.store.data.client.networking.NetworkingContract.RequestBuilder.Companion.BODYLESS_METHODS

internal class RequestBuilder private constructor(
    private val client: HttpClient,
    private val host: String,
    private val protocol: URLProtocol = URLProtocol.HTTPS,
    private val port: Int? = null,
) : NetworkingContract.RequestBuilder {
    private var headers: Header = emptyMap()
    private var parameter: Parameter = emptyMap()
    private var body: Any? = null

    override fun setHeaders(header: Header): NetworkingContract.RequestBuilder {
        return this.also { this.headers = header }
    }

    override fun setParameter(parameter: Parameter): NetworkingContract.RequestBuilder {
        return this.also { this.parameter = parameter }
    }

    override fun setBody(body: Any): NetworkingContract.RequestBuilder {
        return this.also { this.body = body }
    }

    private fun validateBodyAgainstMethod(method: NetworkingContract.Method) {
        if (body != null) {
            if (BODYLESS_METHODS.contains(method)) {
                throw CatClientError.RequestValidationFailure(
                    "${method.name.uppercase()} cannot be combined with a RequestBody.",
                )
            }
        } else {
            if (!BODYLESS_METHODS.contains(method)) {
                throw CatClientError.RequestValidationFailure(
                    "${method.name.uppercase()} must be combined with a RequestBody.",
                )
            }
        }
    }

    private fun setBody(builder: HttpRequestBuilder) {
        if (body != null) {
            builder.setBody(body!!)
        }
    }

    private fun List<String>.resolve(): String {
        return if (size == 1) {
            "/${first()}"
        } else {
            joinToString("/")
        }
    }

    private fun setMandatoryFields(
        builder: HttpRequestBuilder,
        method: NetworkingContract.Method,
        path: Path,
    ) {
        validateBodyAgainstMethod(method)

        builder.host = host
        builder.method = HttpMethod(method.name)
        builder.url.protocol = protocol
        builder.url.path(path.resolve())
        setBody(builder)
    }

    private fun setPort(builder: HttpRequestBuilder) {
        if (port is Int) {
            builder.port = port
        }
    }

    private fun addHeader(builder: HttpRequestBuilder) {
        headers.forEach { (field, value) ->
            builder.header(field, value)
        }
    }

    private fun setParameter(builder: HttpRequestBuilder) {
        parameter.forEach { (field, value) ->
            builder.parameter(field, value)
        }
    }

    private fun buildQuery(
        builder: HttpRequestBuilder,
        method: NetworkingContract.Method,
        path: Path,
    ): HttpRequestBuilder {
        setMandatoryFields(builder, method, path)
        setPort(builder)
        addHeader(builder)
        setParameter(builder)
        return builder
    }

    override fun prepare(
        method: NetworkingContract.Method,
        path: Path,
    ): HttpStatement {
        return HttpStatement(
            buildQuery(
                HttpRequestBuilder(),
                method,
                path,
            ),
            client,
        )
    }

    class Factory constructor(
        private val client: HttpClient,
        private val host: String,
        private val protocol: URLProtocol = URLProtocol.HTTPS,
        private val port: Int? = null,
    ) : NetworkingContract.RequestBuilderFactory {
        override fun create(): NetworkingContract.RequestBuilder {
            return RequestBuilder(
                client,
                host,
                protocol,
                port,
            )
        }
    }
}
