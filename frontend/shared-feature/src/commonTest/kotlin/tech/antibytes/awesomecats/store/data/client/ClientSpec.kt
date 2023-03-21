/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store.data.client

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpStatement
import io.ktor.http.HttpStatusCode
import tech.antibytes.awesomecats.store.data.RepositoryContract
import kotlin.js.JsName
import kotlin.test.BeforeTest
import kotlin.test.Test
import tech.antibytes.awesomecats.store.data.client.error.CatClientError
import tech.antibytes.awesomecats.store.data.client.fixture.StringAlphaGenerator
import tech.antibytes.awesomecats.store.data.client.networking.NetworkingContract
import tech.antibytes.awesomecats.store.data.client.networking.RequestBuilderFactoryMock
import tech.antibytes.awesomecats.store.data.client.networking.RequestBuilderMock
import tech.antibytes.awesomecats.viewmodel.kmock
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kfixture.qualifier.qualifiedBy
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.pixabay.sdk.ClientContract
import tech.antibytes.pixabay.sdk.ConnectivityManagerMock
import tech.antibytes.util.test.coroutine.runBlockingTestWithTimeout
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.ktor.KtorMockClientFactory
import tech.antibytes.util.test.mustBe

@OptIn(KMockExperimental::class)
@KMock(
    NetworkingContract.RequestBuilderFactory::class,
    NetworkingContract.RequestBuilder::class,
    ClientContract.ConnectivityManager::class,
)
class ClientSpec {
    private val ascii = qualifiedBy("ascii")
    private val fixture = kotlinFixture {
        addGenerator(
            String::class,
            StringAlphaGenerator,
            ascii,
        )
    }

    private val requestBuilderFactory: RequestBuilderFactoryMock = kmock()
    private val requestBuilder: RequestBuilderMock = kmock()
    private val connectivityManager: ConnectivityManagerMock = kmock()
    private val ktorDummy = HttpRequestBuilder()

    @BeforeTest
    fun setUp() {
        requestBuilderFactory._clearMock()
        requestBuilder._clearMock()
        connectivityManager._clearMock()
    }

    @Test
    @JsName("fn0")
    fun `It fulfils Client`() {
        CatClient(
            requestBuilderFactory,
            connectivityManager,
        ) fulfils RepositoryContract.Client::class
    }

    @Test
    @JsName("fn1")
    fun `Given fetchCat is called with a query and a page Index it returns an Error if it has no Connection`() = runBlockingTestWithTimeout {
        // Given
        connectivityManager._hasConnection returns false

        // When
        val response = CatClient(
            requestBuilderFactory,
            connectivityManager,
        ).fetchCat()

        // Then
        response mustBe "{}"
    }

    @Test
    @JsName("fn2")
    fun `Given fetchCat is called with a query and a page Index it propagates Errors`() = runBlockingTestWithTimeout {
        // Given
        val expected: String = fixture.fixture()
        val responseError = CatClientError.RequestError(400)
        val client = KtorMockClientFactory.createSimpleMockClient(
            response = expected,
            error = responseError,
            status = HttpStatusCode.BadRequest,
        )

        connectivityManager._hasConnection returns true
        requestBuilderFactory._create returns requestBuilder
        requestBuilder._setParameter returns requestBuilder
        requestBuilder._prepare returns HttpStatement(ktorDummy, client)

        // When
        val response = CatClient(
            requestBuilderFactory,
            connectivityManager,
        ).fetchCat()

        // Then
        response mustBe "{}"
    }

    @Test
    @JsName("fn3")
    fun `Given fetchCat is called with a page Index it delegates the Call and returns the response`() = runBlockingTestWithTimeout {
        // Given
        val expected: String = fixture.fixture()
        val client = KtorMockClientFactory.createSimpleMockClient(
            response = expected,
        )

        connectivityManager._hasConnection returns true
        requestBuilderFactory._create returns requestBuilder
        requestBuilder._prepare returns HttpStatement(ktorDummy, client)

        // When
        val response = CatClient(
            requestBuilderFactory,
            connectivityManager,
        ).fetchCat()

        // Then
        response mustBe expected
    }
}
