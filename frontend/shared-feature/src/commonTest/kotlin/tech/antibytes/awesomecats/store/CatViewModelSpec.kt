/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store

import kotlin.js.JsName
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import tech.antibytes.awesomecats.store.model.FrontendCat
import tech.antibytes.awesomecats.viewmodel.kmock
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.kmock.verification.assertProxy
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.wrapper.coroutine.wrapper.SharedFlowWrapper

@OptIn(KMockExperimental::class)
@KMock(
    CatStoreContract::class,
)
class CatViewModelSpec {
    private val fixture = kotlinFixture()
    private val store: CatStoreContractMock = kmock(
        relaxUnitFun = true,
    )
    private lateinit var scope: TestScope

    @BeforeTest
    fun setup() {
        store._clearMock()
        scope = TestScope()
    }

    @Test
    @JsName("fn0")
    fun `It has a default Cat`() = runTest {
        // Given
        store._catState returns SharedFlowWrapper.getInstance(
            MutableSharedFlow(),
        ) {
            scope
        }

        // When
        val viewModel = CatViewModel(store)

        // Then
        viewModel.cat.value mustBe defaultCat
    }

    @Test
    @JsName("fn1")
    fun `It ignores Initial state value from the store`() = runTest {
        // Given
        val state = CatState.Initial
        val flow = MutableStateFlow<CatState>(
            state,
        )
        store._catState returns SharedFlowWrapper.getInstance(flow) {
            scope
        }

        // When
        val viewModel = CatViewModel(store)
        scope.testScheduler.advanceUntilIdle()

        // Then
        viewModel.cat.value mustBe defaultCat
    }

    @Test
    @JsName("fn2")
    fun `It ignores Pending state value from the store`() = runTest {
        // Given
        val state = CatState.Pending
        val flow = MutableStateFlow<CatState>(
            state,
        )
        store._catState returns SharedFlowWrapper.getInstance(flow) {
            scope
        }

        // When
        val viewModel = CatViewModel(store)
        scope.testScheduler.advanceUntilIdle()

        // Then
        viewModel.cat.value mustBe defaultCat
    }

    @Test
    @JsName("fn3")
    fun `It ignores Error state value from the store`() = runTest {
        // Given
        val state = CatState.Error(
            RuntimeException(),
        )
        val flow = MutableStateFlow<CatState>(
            state,
        )
        store._catState returns SharedFlowWrapper.getInstance(flow) {
            scope
        }

        // When
        val viewModel = CatViewModel(store)
        scope.testScheduler.advanceUntilIdle()

        // Then
        viewModel.cat.value mustBe defaultCat
    }

    @Test
    @JsName("fn4")
    fun `It propagates Accepted state value from the store`() = runTest {
        // Given
        val state = CatState.Accepted(
            FrontendCat(fixture.fixture(), fixture.fixture()),
        )
        val flow = MutableStateFlow<CatState>(
            state,
        )
        store._catState returns SharedFlowWrapper.getInstance(flow) {
            scope
        }

        // When
        val viewModel = CatViewModel(store)
        scope.testScheduler.advanceUntilIdle()

        // Then
        viewModel.cat.value mustBe state.value
    }

    @Test
    @JsName("fn5")
    fun `Given requestCat is called it propagates the call to the Store`() = runTest {
        // Given
        store._catState returns SharedFlowWrapper.getInstance(
            MutableSharedFlow(),
        ) {
            scope
        }

        // When
        val viewModel = CatViewModel(store)
        viewModel.requestCat()
        scope.testScheduler.advanceUntilIdle()

        // Then
        assertProxy {
            store._requestACat.hasBeenCalled()
            store._requestACat.hasNoFurtherInvocations()
        }
    }

    @Test
    @JsName("fn6")
    fun `It fulfils ViewModelContract`() = runTest {
        // Given
        store._catState returns SharedFlowWrapper.getInstance(
            MutableSharedFlow(),
        ) {
            scope
        }

        // When & Then
        CatViewModel(store) fulfils ViewModelContract::class
    }
}
