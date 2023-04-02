/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store.data.client.networking.plugin

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondBadRequest
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.request.request
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertFailsWith
import tech.antibytes.awesomecats.viewmodel.kmock
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fulfils

@OptIn(KMockExperimental::class)
@KMock(
    KtorPluginsContract.ErrorMapper::class,
)
class ResponseValidatorConfiguratorSpec {
    private val fixture = kotlinFixture()

    @Test
    @JsName("fn0")
    fun `It fulfils ResponseValidatorConfigurator`() {
        ResponseValidatorConfigurator() fulfils KtorPluginsContract.ResponseValidatorConfigurator::class
    }

    @Test
    @JsName("fn1")
    fun `Given configure is called  with a Pair of Validators it configures a ErrorPropagator if it is not null`() = runBlockingTest {
        // Given
        val propagator: ErrorMapperMock = kmock()

        propagator._mapAndThrow throws RuntimeException()

        val client = HttpClient(MockEngine) {
            expectSuccess = true
            engine {
                addHandler {
                    respondBadRequest()
                }
            }

            install(HttpCallValidator) {
                ResponseValidatorConfigurator().configure(
                    this,
                    propagator,
                )
            }
        }

        // When & Then
        assertFailsWith<RuntimeException> {
            client.request(fixture.fixture<String>()).body()
        }
    }
}
