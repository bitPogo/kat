/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store.data.client.networking

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.statement.HttpStatement

internal typealias Header = Map<String, String>
internal typealias Parameter = Map<String, Any?>
internal typealias Path = List<String>

internal object NetworkingContract {
    fun interface PluginConfigurator<PluginConfiguration : Any, SubConfiguration> {
        fun configure(pluginConfiguration: PluginConfiguration, subConfiguration: SubConfiguration)
    }

    data class Plugin<PluginConfiguration : Any, SubConfiguration>(
        val feature: HttpClientPlugin<*, *>,
        val pluginConfigurator: PluginConfigurator<PluginConfiguration, SubConfiguration>,
        val subConfiguration: SubConfiguration,
    )

    interface ClientConfigurator {
        fun configure(
            httpConfig: HttpClientConfig<*>,
            installers: Set<Plugin<in Any, in Any?>>? = null,
        )
    }

    enum class Method(val value: String) {
        HEAD("head"),
        DELETE("delete"),
        GET("get"),
        POST("post"),
        PUT("put"),
    }

    interface RequestBuilder {
        fun setHeaders(header: Header): RequestBuilder
        fun setParameter(parameter: Parameter): RequestBuilder
        fun setBody(body: Any): RequestBuilder

        fun prepare(
            method: Method = Method.GET,
            path: Path = listOf(""),
        ): HttpStatement

        companion object {
            val BODYLESS_METHODS = listOf(Method.HEAD, Method.GET)
        }
    }

    interface RequestBuilderFactory {
        fun create(): RequestBuilder
    }
}
