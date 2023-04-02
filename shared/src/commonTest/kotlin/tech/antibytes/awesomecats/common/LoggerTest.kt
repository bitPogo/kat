/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.common

import kotlin.js.JsName
import kotlin.test.Test
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.pixabay.sdk.ClientContract
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

class LoggerTest {
    private val fixture = kotlinFixture()

    @Test
    @JsName("fn0")
    fun `Given info is called it uses the the stdout lambda`() {
        // Given
        val input: String = fixture.fixture()
        var capturedInput: String? = null
        val lambda: Function1<String, Unit> = { givenInput ->
            capturedInput = givenInput
        }

        // When
        Logger(stdout = lambda) { throw RuntimeException() }
            .info(input)

        // Then
        capturedInput mustBe "INFO: $input"
    }

    @Test
    @JsName("fn1")
    fun `Given log is called it uses the the stdout lambda`() {
        // Given
        val input: String = fixture.fixture()
        var capturedInput: String? = null
        val lambda: Function1<String, Unit> = { givenInput ->
            capturedInput = givenInput
        }

        // When
        Logger(stdout = lambda) { throw RuntimeException() }
            .log(input)

        // Then
        capturedInput mustBe "LOG: $input"
    }

    @Test
    @JsName("fn2")
    fun `Given warn is called it uses the the stderr lambda`() {
        // Given
        val input: String = fixture.fixture()
        var capturedInput: String? = null
        val lambda: Function1<String, Unit> = { givenInput ->
            capturedInput = givenInput
        }

        // When
        Logger(stderr = lambda, stdout = { throw RuntimeException() })
            .warn(input)

        // Then
        capturedInput mustBe "WARN: $input"
    }

    @Test
    @JsName("fn3")
    fun `Given error is called it uses the the stderr lambda`() {
        // Given
        val input: String = fixture.fixture()
        val exception = RuntimeException()
        var capturedInput: MutableList<String> = mutableListOf()
        val lambda: Function1<String, Unit> = { givenInput ->
            capturedInput.add(givenInput)
        }

        // When
        Logger(stderr = lambda, stdout = { throw RuntimeException() })
            .error(exception, input)

        // Then
        capturedInput.first() mustBe "ERROR: $input"
        capturedInput.last() mustBe "ERROR: ${exception.stackTraceToString()}"
    }

    @Test
    @JsName("fn4")
    fun `It fulfils ClientLogger`() {
        Logger({}, {}) fulfils ClientContract.Logger::class
    }
}
