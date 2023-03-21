/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store.data.client.networking.plugin

import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import tech.antibytes.awesomecats.store.data.client.serialization.JsonConfiguratorContract

internal class SerializerConfigurator : KtorPluginsContract.SerializerConfigurator {
    override fun configure(
        pluginConfiguration: ContentNegotiation.Config,
        subConfiguration: JsonConfiguratorContract,
    ) {
        pluginConfiguration.json(
            json = Json { subConfiguration.configure(this) },
        )
    }
}
