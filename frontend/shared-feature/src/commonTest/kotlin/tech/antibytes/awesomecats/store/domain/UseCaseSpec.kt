/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store.domain

import kotlin.js.JsName
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import tech.antibytes.awesomecats.store.UseCaseContract
import tech.antibytes.awesomecats.store.model.FrontendCat
import tech.antibytes.awesomecats.viewmodel.kmock
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.sameAs

@OptIn(KMockExperimental::class)
@KMock(
    RepositoryContract::class,
)
class UseCaseSpec {
    private val fixture = kotlinFixture()

    @Test
    @JsName("fn0")
    fun `It fulfils UseCase`() {
        UseCase(kmock()) fulfils UseCaseContract::class
    }

    @Test
    @JsName("fn2")
    fun `Given findACat is called it delegates the call to the Repository and returns its result`() = runTest {
        // Given
        val expected = FrontendCat(
            url = fixture.fixture(),
            purrLevel = fixture.fixture(),
        )
        val repository: RepositoryContractMock = kmock()

        repository._fetchFrontendCat returns expected

        // When
        val actual = UseCase(repository).findACat()

        // Then
        actual sameAs expected
    }
}
