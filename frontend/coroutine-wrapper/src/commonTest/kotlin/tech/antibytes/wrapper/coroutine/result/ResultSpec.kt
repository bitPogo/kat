/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wrapper.coroutine.result

import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertFails
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs

class SuccessSpec {
    private val fixture = kotlinFixture()

    @Test
    @JsName("fn0")
    fun `It fulfils ResultContract`() {
        Success<String, Throwable>(fixture.fixture()) fulfils ResultContract::class
    }

    @Test
    @JsName("fn1")
    fun `Given isSuccess is called it returns true`() {
        // Given
        val succ = Success<String, Throwable>(fixture.fixture())

        // When
        val result = succ.isSuccess()

        // Then
        result mustBe true
    }

    @Test
    @JsName("fn2")
    fun `Given isError is called it returns false`() {
        // Given
        val succ = Success<String, Throwable>(fixture.fixture())

        // When
        val result = succ.isError()

        // Then
        result mustBe false
    }

    @Test
    @JsName("fn3")
    fun `Given unwarp is called it returns the wraped value`() {
        // Given
        val value: String = fixture.fixture()

        val succ = Success<String, Throwable>(value)

        // When
        val result = succ.unwrap()

        // Then
        result mustBe value
    }
}

class FailureSpec {
    @Test
    @JsName("fn0")
    fun `It fulfils ResultContract`() {
        Failure<String, Throwable>(RuntimeException()) fulfils ResultContract::class
    }

    @Test
    @JsName("fn1")
    fun `Given isSuccess is called it returns false`() {
        // Given
        val err = Failure<String, Throwable>(RuntimeException())

        // When
        val result = err.isSuccess()

        // Then
        result mustBe false
    }

    @Test
    @JsName("fn2")
    fun `Given isError is called it returns true`() {
        // Given
        val err = Failure<String, Throwable>(RuntimeException())

        // When
        val result = err.isError()

        // Then
        result mustBe true
    }

    @Test
    @JsName("fn3")
    fun `Given unwarp is called it fails with the wraped value`() {
        // Given
        val expected = RuntimeException()

        val err = Failure<String, Throwable>(expected)

        // Then
        val result = assertFails {
            err.unwrap()
        }

        result sameAs expected
    }
}
