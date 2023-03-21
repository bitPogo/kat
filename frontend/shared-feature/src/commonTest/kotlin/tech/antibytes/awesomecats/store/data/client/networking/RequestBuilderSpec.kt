/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store.data.client.networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteReadPacket
import io.ktor.client.request.HttpRequestData
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.URLProtocol
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import io.ktor.util.toMap
import kotlin.js.JsName
import kotlin.math.absoluteValue
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlinx.coroutines.GlobalScope
import tech.antibytes.awesomecats.store.data.client.error.CatClientError
import tech.antibytes.awesomecats.store.data.client.fixture.StringAlphaGenerator
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kfixture.listFixture
import tech.antibytes.kfixture.pairFixture
import tech.antibytes.kfixture.qualifier.qualifiedBy
import tech.antibytes.util.test.coroutine.runBlockingTestInContext
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.ktor.KtorMockClientFactory
import tech.antibytes.util.test.mustBe

class RequestBuilderSpec {
    private val ascii = qualifiedBy("ascii")
    private val fixture = kotlinFixture {
        addGenerator(
            String::class,
            StringAlphaGenerator,
            ascii,
        )
    }
    private val host: String = fixture.fixture()

    private fun createMockClientWithAssertion(assert: (HttpRequestData) -> Unit): HttpClient {
        return HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    assert.invoke(request)
                    respond(
                        fixture.fixture<String>(ascii),
                        headers = headersOf(
                            "Content-Type" to listOf(
                                ContentType.Text.Plain.toString(),
                            ),
                        ),
                    )
                }
            }
        }
    }

    @Test
    @JsName("fn0")
    fun `It fulfils RequestBuilderFactory`() {
        // Given
        val client = KtorMockClientFactory.createSimpleMockClient(fixture.fixture())

        // When
        val builder: Any = RequestBuilder.Factory(
            client,
            fixture.fixture(ascii),
        )

        // Then
        builder fulfils NetworkingContract.RequestBuilderFactory::class
    }

    @Test
    @JsName("fn1")
    fun `Given create is called it returns a RequestBuilder`() {
        // Given
        val client = KtorMockClientFactory.createSimpleMockClient(fixture.fixture())

        // When
        val builder: Any = RequestBuilder.Factory(
            client,
            fixture.fixture(ascii),
        ).create()

        // Then
        builder fulfils NetworkingContract.RequestBuilder::class
    }

    @Test
    @JsName("fn2")
    fun `Given a Request was prepared and executed it uses GET by default`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val client = createMockClientWithAssertion { request ->
            // Then
            request.method mustBe HttpMethod.Get
        }

        // When
        RequestBuilder.Factory(
            client,
            fixture.fixture(ascii),
        ).create().prepare().execute()
    }

    @Test
    @JsName("fn3")
    fun `Given a Request was prepared and executed it calls the given Host`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val host: String = fixture.fixture(ascii)
        val client = createMockClientWithAssertion { request ->
            // Then
            request.url.host mustBe host
        }

        // When
        RequestBuilder.Factory(
            client,
            host,
        ).create().prepare().execute()
    }

    @Test
    @JsName("fn4")
    fun `Given a Request was prepared and executed it calls the root by default`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val client = createMockClientWithAssertion { request ->
            // Then
            request.url.fullPath mustBe "/"
        }

        // When
        RequestBuilder.Factory(
            client,
            fixture.fixture(ascii),
        ).create().prepare().execute()
    }

    @Test
    @JsName("fn5")
    fun `Given a Request was prepared and executed with a Path it calls the given path`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val path = fixture.listFixture<String>(ascii, size = 3)

        val client = createMockClientWithAssertion { request ->
            // Then
            request.url.fullPath mustBe "/${path.joinToString("/")}"
        }

        // When
        RequestBuilder.Factory(
            client,
            fixture.fixture(ascii),
        ).create().prepare(path = path).execute()
    }

    @Test
    @JsName("fn6")
    fun `Given a Request was executed it calls the given Host via HTTPS`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val client = createMockClientWithAssertion { request ->
            // Then
            request.url.protocol mustBe URLProtocol.HTTPS
        }

        // When
        RequestBuilder.Factory(
            client,
            fixture.fixture(ascii),
        ).create().prepare().execute()
    }

    @Test
    @JsName("fn7")
    fun `Given a Request was executed it calls the given Host and HTTP if instructed to`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val client = createMockClientWithAssertion { request ->
            // Then
            request.url.protocol mustBe URLProtocol.HTTP
        }

        // When
        RequestBuilder.Factory(
            client,
            host,
            protocol = URLProtocol.HTTP,
        ).create().prepare().execute()
    }

    @Test
    @JsName("fn8")
    fun `Given a Request was prepared and executed it uses the default Port`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val client = createMockClientWithAssertion { request ->
            // Then
            request.url.port mustBe URLProtocol.HTTPS.defaultPort
        }

        // When
        RequestBuilder.Factory(
            client,
            fixture.fixture(ascii),
        ).create().prepare().execute()
    }

    @Test
    @JsName("fn9")
    fun `Given a instance was create with a Environment and a Port and it was prepared and executed it uses the given Port`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val port = fixture.fixture<Short>().toInt().absoluteValue
        val host: String = fixture.fixture(ascii)

        val client = createMockClientWithAssertion { request ->
            // Then
            request.url.port mustBe port
        }

        // When
        RequestBuilder.Factory(
            client,
            host,
            port = port,
        ).create().prepare().execute()
    }

    @Test
    @JsName("fn10")
    fun `Given a Request was prepared and executed it sets no custom headers to the request by default`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val client = createMockClientWithAssertion { request ->
            // Then
            request.headers.toMap() mustBe mapOf(
                "Accept-Charset" to listOf("UTF-8"),
                "Accept" to listOf("*/*"),
            )
        }

        // When
        RequestBuilder.Factory(
            client,
            fixture.fixture(ascii),
        ).create().prepare().execute()
    }

    @Test
    @JsName("fn11")
    fun `Given a instance was create with a Environment setHeaders was called with Headers and it was prepared and executed itsets the given headers to the request`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val headers = mapOf<String, String>(
            fixture.pairFixture(ascii, ascii),
            fixture.pairFixture(ascii, ascii),
        )

        val keys = headers.keys.toList()
        val host: String = fixture.fixture(ascii)
        val client = createMockClientWithAssertion { request ->
            // Then
            request.headers.toMap() mustBe mapOf(
                "Accept-Charset" to listOf("UTF-8"),
                "Accept" to listOf("*/*"),
                keys[0] to listOf(headers[keys[0]]),
                keys[1] to listOf(headers[keys[1]]),
            )
        }

        // When
        RequestBuilder.Factory(
            client,
            host,
        ).create().setHeaders(headers).prepare().execute()
    }

    @Test
    @JsName("fn12")
    fun `Given a Request was prepared and executed itsets no custom parameter to the request by default`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val client = createMockClientWithAssertion { request ->
            // Then
            request.url.parameters.toMap() mustBe emptyMap()
        }

        // When
        RequestBuilder.Factory(
            client,
            fixture.fixture(ascii),
        ).create().prepare().execute()
    }

    @Test
    @JsName("fn13")
    fun `Given a instance was create with a Environment setParameter was called with parameter and it was prepared and executed itsets custom parameter to the request`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val parameter = mapOf<String, String>(
            fixture.pairFixture(ascii, ascii),
            fixture.pairFixture(ascii, ascii),
        )

        val keys = parameter.keys.toList()

        val host: String = fixture.fixture(ascii)
        val client = createMockClientWithAssertion { request ->
            // Then
            request.url.parameters.toMap() mustBe mapOf(
                keys[0] to listOf(parameter[keys[0]]),
                keys[1] to listOf(parameter[keys[1]]),
            )
        }

        // When
        RequestBuilder.Factory(
            client,
            host,
        ).create().setParameter(parameter).prepare().execute()
    }

    @Test
    @JsName("fn14")
    fun `Given a Request was prepared and executed ithas no Body by default`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val client = createMockClientWithAssertion { request ->
            // Then
            request.body.contentLength!! mustBe 0
        }

        // When
        RequestBuilder.Factory(
            client,
            fixture.fixture(ascii),
        ).create().prepare().execute()
    }

    @Test
    @JsName("fn15")
    fun `Given a Requests setBody is called with a Payload and it was prepared and executed with GET it fails`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val client = KtorMockClientFactory.createSimpleMockClient(fixture.fixture(ascii))

        // Then
        val error = assertFailsWith<CatClientError.RequestValidationFailure> {
            // When
            RequestBuilder.Factory(
                client,
                host,
            ).create().setBody(fixture.fixture<String>(ascii)).prepare(NetworkingContract.Method.GET)
        }

        // Then
        error.message!! mustBe "GET cannot be combined with a RequestBody."
    }

    @Test
    @JsName("fn16")
    fun `Given a Requests setBody is called with a Payload and it was prepared and executed with HEAD it fails`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val client = KtorMockClientFactory.createSimpleMockClient(fixture.fixture())

        // Then
        val error = assertFailsWith<CatClientError.RequestValidationFailure> {
            // When
            RequestBuilder.Factory(
                client,
                host,
            ).create().setBody(fixture.fixture<String>(ascii)).prepare(NetworkingContract.Method.HEAD)
        }

        // Then
        error.message!! mustBe "HEAD cannot be combined with a RequestBody."
    }

    @Test
    @JsName("fn17")
    fun `Given a Requests setBody was not called and it was prepared and executed with POST it fails`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val client = KtorMockClientFactory.createSimpleMockClient(fixture.fixture(ascii))

        // Then
        val error = assertFailsWith<CatClientError.RequestValidationFailure> {
            // When
            RequestBuilder.Factory(
                client,
                host,
            ).create().prepare(NetworkingContract.Method.POST)
        }

        // Then
        error.message!! mustBe "POST must be combined with a RequestBody."
    }

    @Test
    @JsName("fn18")
    fun `Given a Requests setBody was not called and it was prepared and executed with PUT it fails`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val client = KtorMockClientFactory.createSimpleMockClient(fixture.fixture(ascii))

        // Then
        val error = assertFailsWith<CatClientError.RequestValidationFailure> {
            // When
            RequestBuilder.Factory(
                client,
                host,
            ).create().prepare(NetworkingContract.Method.PUT)
        }

        // Then
        error.message!! mustBe "PUT must be combined with a RequestBody."
    }

    @Test
    @JsName("fn19")
    fun `Given Requests setBody was not called and it was prepared and executed with DELETE it fails`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val client = KtorMockClientFactory.createSimpleMockClient(fixture.fixture(ascii))

        // Then
        val error = assertFailsWith<CatClientError.RequestValidationFailure> {
            // When
            RequestBuilder.Factory(
                client,
                host,
            ).create().prepare(NetworkingContract.Method.DELETE)
        }

        // Then
        error.message!! mustBe "DELETE must be combined with a RequestBody."
    }

    @Test
    @JsName("fn20")
    fun `Given a Request was prepared and executed with HEAD it uses head`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val client = createMockClientWithAssertion { request ->
            // Then
            request.method mustBe HttpMethod.Head
        }

        // When
        RequestBuilder.Factory(
            client,
            fixture.fixture(ascii),
        ).create().prepare(NetworkingContract.Method.HEAD).execute()
    }

    @Test
    @JsName("fn21")
    fun `Given a Requests setBody was called with a Payload and it was prepared and executed with POST it uses post`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val payload: String = fixture.fixture(ascii)

        val client = createMockClientWithAssertion { request ->
            // Then
            request.method mustBe HttpMethod.Post
        }

        // When
        RequestBuilder.Factory(
            client,
            host,
        ).create().setBody(payload).prepare(NetworkingContract.Method.POST).execute()
    }

    @Test
    @JsName("fn22")
    fun `Given a Requests setBody was called with a Payload and it was prepared and executed with POST it attaches the body to the request`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val payload: String = fixture.fixture(ascii)

        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    // Then
                    request.body.toByteReadPacket().readText() mustBe payload
                    respond(fixture.fixture<String>(ascii))
                }
            }
        }

        // When
        RequestBuilder.Factory(
            client,
            host,
        ).create().setBody(payload).prepare(NetworkingContract.Method.POST).execute()
    }

    @Test
    @JsName("fn23")
    fun `Given a Requests setBody was called with a Payload and it was prepared and executed with PUT it uses put`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val payload: String = fixture.fixture(ascii)

        val client = createMockClientWithAssertion { request ->
            // Then
            request.method mustBe HttpMethod.Put
        }

        // When
        RequestBuilder.Factory(
            client,
            host,
        ).create().setBody(payload).prepare(NetworkingContract.Method.PUT).execute()
    }

    @Test
    @JsName("fn24")
    fun `Given a Requests setBody was called with a Payload and it was prepared and executed with PUT it attaches the body to the request`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val payload: String = fixture.fixture(ascii)

        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    // Then
                    request.body.toByteReadPacket().readText() mustBe payload
                    respond(fixture.fixture<String>(ascii))
                }
            }
        }

        // When
        RequestBuilder.Factory(
            client,
            host,
        ).create().setBody(payload).prepare(NetworkingContract.Method.PUT).execute()
    }

    @Test
    @JsName("fn26")
    fun `Given a Requests setBody was called with a Payload and it was prepared and executed with DELETE it uses delete`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val payload: String = fixture.fixture(ascii)

        val client = createMockClientWithAssertion { request ->
            // Then
            request.method mustBe HttpMethod.Delete
        }

        // When
        RequestBuilder.Factory(
            client,
            host,
        ).create().setBody(payload).prepare(NetworkingContract.Method.DELETE).execute()
    }

    @Test
    @JsName("fn27")
    fun `Given a Requests setBody was called with a Payload and it was prepared and executed with DELETE it attaches the body to the request`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val payload: String = fixture.fixture(ascii)

        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    // Then
                    request.body.toByteReadPacket().readText() mustBe payload

                    respond(fixture.fixture<String>(ascii))
                }
            }
        }

        // When
        RequestBuilder.Factory(
            client,
            host,
        ).create().setBody(payload).prepare(NetworkingContract.Method.DELETE).execute()
    }
}
