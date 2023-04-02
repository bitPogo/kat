/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store.di

import kotlin.js.JsName
import kotlin.random.Random
import kotlin.test.Test
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.TestScope
import tech.antibytes.awesomecats.store.CatState
import tech.antibytes.awesomecats.store.UseCaseContract
import tech.antibytes.awesomecats.store.data.RepositoryContract.Client
import tech.antibytes.awesomecats.store.domain.RepositoryContract
import tech.antibytes.awesomecats.viewmodel.kmock
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.pixabay.sdk.ClientContract
import tech.antibytes.util.test.isNot
import tech.antibytes.wrapper.coroutine.wrapper.CoroutineWrapperContract.CoroutineScopeDispatcher
import tech.antibytes.wrapper.coroutine.wrapper.CoroutineWrapperContract.SharedFlowWrapper

@OptIn(KMockExperimental::class)
@KMock(
    ClientContract.Logger::class,
    ClientContract.ConnectivityManager::class,
    CoroutineScopeDispatcher::class,
)
class CatStoreKoinSpec {
    private val fixture = kotlinFixture()

    @Test
    @JsName("fn0")
    fun `Given initKoin is called it contains Random`() {
        // When
        val koin = initKoin(
            kmock(),
            kmock(),
            fixture.fixture(),
            kmock(),
            kmock(),
        )

        // Then
        koin.koin.get<Random>() isNot null
    }

    @Test
    @JsName("fn2")
    fun `Given initKoin is called it contains Client`() {
        // When
        val koin = initKoin(
            kmock(),
            kmock(),
            fixture.fixture(),
            kmock(),
            kmock(),
        )

        // Then
        koin.koin.get<Client>() isNot null
    }

    @Test
    @JsName("fn3")
    fun `Given initKoin is called it contains ProducerScope`() {
        // When
        val koin = initKoin(
            kmock(),
            kmock(),
            fixture.fixture(),
            kmock(),
            kmock(),
        )

        // Then
        koin.koin.get<CoroutineScopeDispatcher>() isNot null
    }

    @Test
    @JsName("fn4")
    fun `Given initKoin is called it contains Repository`() {
        // When
        val koin = initKoin(
            kmock(),
            kmock(),
            fixture.fixture(),
            kmock(),
            kmock(),
        )

        // Then
        koin.koin.get<RepositoryContract>() isNot null
    }

    @Test
    @JsName("fn5")
    fun `Given initKoin is called it contains Usecase`() {
        // When
        val koin = initKoin(
            kmock(),
            kmock(),
            fixture.fixture(),
            kmock(),
            kmock(),
        )

        // Then
        koin.koin.get<UseCaseContract>() isNot null
    }

    @Test
    @JsName("fn6")
    fun `Given initKoin is called it contains MutableStateFlow`() {
        // When
        val koin = initKoin(
            kmock(),
            kmock(),
            fixture.fixture(),
            kmock(),
            kmock(),
        )

        // Then
        koin.koin.get<MutableStateFlow<CatState>>() isNot null
    }

    @Test
    @JsName("fn7")
    fun `Given initKoin is called it contains CoroutineWrapperContractSharedFlowWrapper`() {
        // When
        val koin = initKoin(
            kmock(),
            kmock(),
            fixture.fixture(),
            kmock(),
            { TestScope() },
        )

        // Then
        koin.koin.get<SharedFlowWrapper<CatState>>() isNot null
    }
}
