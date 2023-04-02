/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store

import kotlin.js.JsName
import kotlin.test.Test
import tech.antibytes.awesomecats.viewmodel.kmock
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.pixabay.sdk.ClientContract
import tech.antibytes.util.test.fulfils
import tech.antibytes.wrapper.coroutine.wrapper.CoroutineWrapperContract

@OptIn(KMockExperimental::class)
@KMock(
    ClientContract.Logger::class,
    ClientContract.ConnectivityManager::class,
    CoroutineWrapperContract.CoroutineScopeDispatcher::class,
)
class CatStoreFactorySpec {
    private val fixture = kotlinFixture()

    @Test
    @JsName("fn0")
    fun `Given getInstance is called it returns an CatStore`() {
        // When
        val actual = CatStore.getInstance(
            kmock(),
            kmock(),
            fixture.fixture(),
            kmock(),
            kmock(),
        )

        // Then
        actual fulfils CatStoreContract::class
    }

    @Test
    @JsName("fn1")
    fun `It fulfils CatStoreFactoryContract`() {
        CatStore fulfils CatStoreFactoryContract::class
    }
}
