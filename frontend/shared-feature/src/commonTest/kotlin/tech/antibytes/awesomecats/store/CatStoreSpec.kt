/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store

import kotlin.js.JsName
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import tech.antibytes.awesomecats.store.model.FrontendCat
import tech.antibytes.awesomecats.viewmodel.kmock
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.kmock.verification.assertProxy
import tech.antibytes.util.test.coroutine.AsyncTestReturnValue
import tech.antibytes.util.test.coroutine.runBlockingTestWithTimeout
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import tech.antibytes.wrapper.coroutine.wrapper.CoroutineWrapperContract.CoroutineScopeDispatcher
import tech.antibytes.wrapper.coroutine.wrapper.CoroutineWrapperContract.SharedFlowWrapper
import tech.antibytes.wrapper.coroutine.wrapper.SharedFlowWrapperMock

@OptIn(KMockExperimental::class)
@KMock(
    UseCaseContract::class,
    CoroutineScopeDispatcher::class,
    SharedFlowWrapper::class,
)
class CatStoreSpec {
    private val fixture = kotlinFixture()
    private val scheduler = TestCoroutineScheduler()
    private lateinit var storeScope: CoroutineScope
    private val usecase: UseCaseContractMock = kmock()
    private val flow: SharedFlowWrapperMock<CatState> = kmock(
        templateType = SharedFlowWrapper::class,
    )

    @BeforeTest
    fun setup() {
        usecase._clearMock()

        storeScope = TestScope(scheduler)
    }

    @Test
    @JsName("fn1")
    fun `Given requestACat is called delegates the call to the usecase and emits its error`(): AsyncTestReturnValue {
        // Given
        val expected = FrontendCat(
            url = "",
            purrLevel = fixture.fixture(),
        )

        val result = Channel<CatState>(
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
            capacity = 1,
        )
        val catFlow: MutableStateFlow<CatState> = MutableStateFlow(
            CatState.Initial,
        )

        val koin = koinApplication {
            modules(
                module {
                    single { catFlow }
                    single { flow }
                    single<UseCaseContract> { usecase }
                    single { CoroutineScopeDispatcher { storeScope } }
                },
            )
        }

        catFlow.onEach { state -> result.send(state) }.launchIn(storeScope)

        usecase._findACat returns expected

        // When
        val store = CatStore(koin)
        scheduler.advanceUntilIdle()

        // Then
        runBlockingTestWithTimeout {
            result.receive() sameAs CatState.Initial
        }

        // When
        store.requestACat()
        scheduler.advanceUntilIdle()

        // Then
        return runBlockingTestWithTimeout {
            val success = result.receive()

            success fulfils CatState.Error::class
            ((success as CatState.Error).error is RuntimeException) mustBe true

            assertProxy {
                usecase._findACat.hasBeenCalled()
            }
        }
    }

    @Test
    @JsName("fn2")
    fun `Given requestACat is called delegates the call to the usecase and emits its response`(): AsyncTestReturnValue {
        // Given
        val expected = FrontendCat(
            url = fixture.fixture(),
            purrLevel = fixture.fixture(),
        )

        val result = Channel<CatState>(
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
            capacity = 1,
        )
        val catFlow: MutableStateFlow<CatState> = MutableStateFlow(
            CatState.Initial,
        )

        val koin = koinApplication {
            modules(
                module {
                    single { catFlow }
                    single { flow }
                    single<UseCaseContract> { usecase }
                    single { CoroutineScopeDispatcher { storeScope } }
                },
            )
        }

        catFlow.onEach { state -> result.send(state) }.launchIn(storeScope)

        usecase._findACat returns expected

        // When
        val store = CatStore(koin)
        scheduler.advanceUntilIdle()

        // Then
        runBlockingTestWithTimeout {
            result.receive() sameAs CatState.Initial
        }

        // When
        store.requestACat()
        scheduler.advanceUntilIdle()

        // Then
        return runBlockingTestWithTimeout {
            val success = result.receive()

            success fulfils CatState.Accepted::class
            (success as CatState.Accepted).value sameAs expected

            assertProxy {
                usecase._findACat.hasBeenCalled()
            }
        }
    }

    @Test
    @JsName("fn0")
    fun `It fulfils CatStoreContract`() {
        CatStore(koinApplication()) fulfils CatStoreContract::class
    }
}
