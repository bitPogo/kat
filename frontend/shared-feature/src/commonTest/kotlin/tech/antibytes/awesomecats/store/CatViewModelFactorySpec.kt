/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store

import kotlin.js.JsName
import kotlin.test.Test
import kotlinx.coroutines.test.TestScope
import tech.antibytes.awesomecats.viewmodel.kmock
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.pixabay.sdk.ClientContract
import tech.antibytes.util.test.fulfils

@OptIn(KMockExperimental::class)
@KMock(
    ClientContract.Logger::class,
    ClientContract.ConnectivityManager::class,
)
class CatViewModelFactorySpec {
    private val fixture = kotlinFixture()

    @Test
    @JsName("fn0")
    fun `Given getInstance is called it returns an CatViewModel`() {
        // When
        val actual = CatViewModel.getInstance(
            kmock(),
            kmock(),
            fixture.fixture(),
            { TestScope() },
            { TestScope() },
        )

        // Then
        actual fulfils ViewModelContract::class
    }

    @Test
    @JsName("fn1")
    fun `It fulfils CatStoreFactoryContract`() {
        CatViewModel fulfils ViewModelFactoryContract::class
    }
}
