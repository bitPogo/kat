/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wrapper.coroutine.wrapper

import kotlin.js.JsName
import kotlin.test.Test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs

class SuspendingFunctionWrapperSpec {
    @Test
    @JsName("fn0")
    fun `It fulfils SuspendingFunctionWrapperFactory`() {
        SuspendingFunctionWrapper fulfils CoroutineWrapperContract.SuspendingFunctionWrapperFactory::class
    }

    @Test
    @JsName("fn1")
    fun `Given getInstance is called with a CoroutineScope and a supending Function it returns a SuspendingFunctionWrapper`() {
        SuspendingFunctionWrapper.getInstance(
            suspend { /* Do nothing*/ },
            { CoroutineScope(Dispatchers.Default) },
        ) fulfils CoroutineWrapperContract.SuspendingFunctionWrapper::class
    }

    @Test
    @JsName("fn2")
    fun `It exposes its wrapped supending Function`() {
        // Given
        val function = suspend { /* Do nothing*/ }

        // When
        val result = SuspendingFunctionWrapper.getInstance(
            function,
            { GlobalScope },
        ).wrappedFunction

        // Then
        result sameAs function
    }

    @Test
    @JsName("fn3")
    fun `Given subscribe is called it returns a Job`() {
        // Given
        val function = suspend { /* Do nothing*/ }

        // When
        val result = SuspendingFunctionWrapper.getInstance(
            function,
            { GlobalScope },
        ).subscribe({}, {})

        // Then
        result fulfils Job::class
    }

    @Test
    @JsName("fn4")
    fun `Given subscribe is called with a Closure to receive Item it delegates the emitted Item to there`() {
        // Given
        val item = object {}
        val function = suspend { item }

        val capturedItem = Channel<Any>()

        // When
        val job = SuspendingFunctionWrapper.getInstance(
            function,
            { GlobalScope },
        ).subscribe(
            { delegatedItem ->
                CoroutineScope(Dispatchers.Default).launch {
                    capturedItem.send(delegatedItem)
                }
            },
            {},
        )

        // Then
        runBlockingTest {
            withTimeout(2000) {
                job.join()

                capturedItem.receive() sameAs item
            }
        }
    }

    @Test
    @JsName("fn5")
    fun `Given subscribe is called with a Closure to receive Errors it delegates the emitted Error to there`() {
        // Given
        val exception = RuntimeException()
        val function = suspend { throw exception }

        val capturedError = Channel<Throwable>()

        // When
        val job = SuspendingFunctionWrapper.getInstance(
            function,
            { GlobalScope },
        ).subscribe(
            {},
            { error ->
                CoroutineScope(Dispatchers.Default).launch {
                    capturedError.send(error)
                }
            },
        )

        // Then
        runBlockingTest {
            withTimeout(2000) {
                job.join()

                capturedError.receive() sameAs exception
            }
        }
    }

    @Test
    @JsName("fn6")
    fun `Given subscribe is called in a scope it launches in it`() {
        // Given
        val scope = testScope1
        val function = suspend { delay(5000) }

        // When
        val job = SuspendingFunctionWrapper.getInstance(
            function,
            { scope },
        ).subscribe({}, {})

        // Then
        job.isActive mustBe true
        scope.cancel()
        job.isActive mustBe false
    }
}
