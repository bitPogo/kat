/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.backend.pixabay.domain.usecase

import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import tech.antibytes.awesomecats.backend.kmock
import tech.antibytes.awesomecats.common.entity.Cat
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.util.test.fulfils

@OptIn(KMockExperimental::class)
@KMock(
    RepositoryContract::class,
)
class UsecaseTest {
    @Test
    fun `It fulfils Usecase`() {
        val usecase: Any = Usecase(kmock())

        usecase fulfils UsecaseContract::class
    }

    @Test
    fun `Given resolveCat is called it resolves a Cat`() = runTest {
        // Given
        val repo: RepositoryContractMock = kmock()
        val usecase = Usecase(repo)
        val expected = Cat(
            id = 1231313,
            url = "url",
        )

        repo._fetchCat returns expected

        // When
        /*usecase.resolveCat().collect { actual ->
            // Then
            assertEquals(
                actual,
                expected,
            )
        }*/
    }
}
