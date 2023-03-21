/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store.data.client.networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.plugins.HttpClientPluginMock
import io.ktor.util.AttributeKey
import kotlin.js.JsName
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.GlobalScope
import tech.antibytes.awesomecats.viewmodel.kmock
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.kmock.verification.assertProxy
import tech.antibytes.util.test.coroutine.runBlockingTestInContext
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

@OptIn(KMockExperimental::class)
@KMock(
    NetworkingContract.PluginConfigurator::class,
    HttpClientPlugin::class,
)
class ClientConfiguratorSpec {
    private val fixture = kotlinFixture()
    var amount = 0

    @BeforeTest
    fun setUp() {
        amount = 0
    }

    @Test
    @JsName("fn0")
    fun `It fulfils ClientConfigurator`() {
        ClientConfigurator fulfils NetworkingContract.ClientConfigurator::class
    }

    @Test
    @JsName("fn1a")
    fun `Given configure is called with a ClientConfigurator and null it does nothing`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val plugin: HttpClientPluginMock<Any, Any> = kmock(
            templateType = HttpClientPlugin::class,
            relaxUnitFun = true,
        )
        val config: Any = fixture.fixture()

        plugin._key returns AttributeKey(fixture.fixture())
        plugin._prepare run { configAction ->
            config.apply(configAction)
        }

        val pluginConfigurator: PluginConfiguratorMock<Any, Any?> = kmock(
            relaxUnitFun = true,
            templateType = NetworkingContract.PluginConfigurator::class,
        )

        // When
        HttpClient(MockEngine) {
            ClientConfigurator.configure(
                this,
                null,
            )

            engine {
                addHandler {
                    respond(fixture.fixture<String>())
                }
            }
        }

        // Then
        assertProxy {
            pluginConfigurator._configure.hasNoFurtherInvocations()
        }
    }

    @Test
    @JsName("fn1")
    fun `Given configure is called with a ClientConfigurator and a Set of Plugin it installs a given Plugin`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val plugin: HttpClientPluginMock<Any, Any> = kmock(
            templateType = HttpClientPlugin::class,
            relaxUnitFun = true,
        )
        val config: Any = fixture.fixture()

        plugin._key returns AttributeKey(fixture.fixture())
        plugin._prepare run { configAction ->
            config.apply(configAction)
        }

        val subConfig: String = fixture.fixture()
        val pluginConfigurator: PluginConfiguratorMock<Any, Any?> = kmock(
            relaxUnitFun = true,
            templateType = NetworkingContract.PluginConfigurator::class,
        )

        val plugins = setOf(
            NetworkingContract.Plugin(
                plugin,
                pluginConfigurator,
                subConfig,
            ),
        )

        // When
        HttpClient(MockEngine) {
            ClientConfigurator.configure(
                this,
                plugins,
            )

            engine {
                addHandler {
                    respond(fixture.fixture<String>())
                }
            }
        }

        // Then
        assertProxy {
            pluginConfigurator._configure.hasBeenCalledWith(
                config,
                subConfig,
            )
        }
    }

    @Test
    @JsName("fn2")
    fun `Given configure is called with a ClientConfigurator and a List of HttpFeatureInstaller it installs a arbitrary number of Plugins`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val pluginConfigurator: PluginConfiguratorMock<Any, Any?> = kmock(
            relaxUnitFun = true,
            templateType = NetworkingContract.PluginConfigurator::class,
        )
        val plugin: HttpClientPluginMock<Any, Any> = kmock(
            templateType = HttpClientPlugin::class,
            relaxUnitFun = true,
        )
        plugin._key returns AttributeKey(fixture.fixture())
        plugin._prepare run { configAction ->
            fixture.fixture<Any>().apply(configAction)
        }

        pluginConfigurator._configure run { _, _ ->
            amount++
        }

        val plugins = setOf(
            NetworkingContract.Plugin(
                plugin,
                pluginConfigurator,
                fixture.fixture<Any>(),
            ),
            NetworkingContract.Plugin(
                plugin,
                pluginConfigurator,
                fixture.fixture<Any>(),
            ),
            NetworkingContract.Plugin(
                plugin,
                pluginConfigurator,
                fixture.fixture<Any>(),
            ),
        )

        // When
        HttpClient(MockEngine) {
            ClientConfigurator.configure(
                this,
                plugins,
            )

            engine {
                addHandler {
                    respond(fixture.fixture<String>())
                }
            }
        }

        // Then
        amount mustBe plugins.size
    }
}
