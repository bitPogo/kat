/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wrapper.coroutine.wrapper

import kotlin.js.JsName
import kotlin.test.Test
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.coroutine.runBlockingTestWithTimeout
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.wrapper.coroutine.result.ResultContract
import tech.antibytes.wrapper.coroutine.result.State
import tech.antibytes.wrapper.coroutine.result.Success

class SharedFlowWrapperSpec {
    private val fixture = kotlinFixture()

    @Test
    @JsName("fn0")
    fun `It fulfils SharedFlowWrapperFactory`() {
        SharedFlowWrapper fulfils CoroutineWrapperContract.SharedFlowWrapperFactory::class
    }

    @Test
    @JsName("fn1")
    fun `It fulfils SharedFlowWrapper`() {
        SharedFlowWrapper.getInstance<State>(
            MutableSharedFlow(),
            { GlobalScope },
        ) fulfils CoroutineWrapperContract.SharedFlowWrapper::class
    }

    @Test
    @JsName("fn2")
    fun `Given subscribe is called, with a onEach parameter, it channels emitted values to the subscriber`() {
        // Given
        val flow = MutableSharedFlow<ResultContract<String, RuntimeException>>()
        val expected = Success<String, RuntimeException>(fixture.fixture())
        val channel = Channel<ResultContract<String, RuntimeException>>()

        // When
        val wrapped = SharedFlowWrapper.getInstance(
            flow,
            { testScope1 },
        )

        wrapped.subscribe { value: ResultContract<String, RuntimeException> ->
            testScope1.launch {
                channel.send(value)
            }

            Unit
        }

        runBlockingTest {
            testScope2.launch {
                flow.emit(expected)
            }
        }

        // Then
        runBlockingTestWithTimeout {
            channel.receive() mustBe expected
        }
    }

    @Test
    @JsName("fn3")
    fun `Given subscribeWithSuspending is called, with a onEach Parameter, which is supspending, it channels emitted values to the subscriber`() {
        // Given
        val flow = MutableSharedFlow<ResultContract<String, RuntimeException>>()
        val expected = Success<String, RuntimeException>(fixture.fixture())
        val channel = Channel<ResultContract<String, RuntimeException>>()

        // When
        val wrapped = SharedFlowWrapper.getInstance(
            flow,
            { testScope1 },
        )

        wrapped.subscribeWithSuspendingFunction { result ->
            channel.send(result)
        }

        runBlockingTest {
            testScope1.launch {
                flow.emit(expected)
            }
        }

        // Then
        runBlockingTestWithTimeout {
            channel.receive() mustBe expected
        }
    }

    @Test
    @JsName("fn4")
    fun `It exposes its Cache`() {
        // Given
        val flow = MutableSharedFlow<ResultContract<String, RuntimeException>>(replay = 1)
        val expected = Success<String, RuntimeException>(fixture.fixture())

        // When
        val wrapped = SharedFlowWrapper.getInstance(
            flow,
            { testScope1 },
        )

        wrapped.subscribe { }

        runBlockingTest {
            testScope2.launch {
                flow.emit(expected)
            }
        }

        // Then
        runBlockingTest {
            wrapped.replayCache mustBe listOf(expected)
        }
    }
}
