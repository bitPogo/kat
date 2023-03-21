/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.backend.pixabay.data.repository

import com.github.michaelbull.result.Ok
import kotlin.properties.Delegates
import kotlin.random.Random
import kotlinx.coroutines.test.runTest
import tech.antibytes.awesomecats.backend.kmock
import tech.antibytes.awesomecats.backend.pixabay.domain.usecase.RepositoryContract
import tech.antibytes.awesomecats.common.entity.Cat
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.pixabay.sdk.model.PixabayItem
import tech.antibytes.pixabay.sdk.model.PixabayResponse
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.Test

@OptIn(KMockExperimental::class)
@KMock(
    DataSourceContract::class,
)
class RepositoryTest {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils Repository`() {
        Repository(kmock(), TestRandom(2)) fulfils RepositoryContract::class
    }

    @Test
    fun `Given fetchCat is called it returns a random Cat`() = runTest {
        // Given
        val random: Random = TestRandom(2)

        val expected = Cat(
            id = fixture.fixture(),
            url = fixture.fixture(),
        )
        val dto = PixabayResponse(
            total = fixture.fixture(),
            items = listOf(
                PixabayItem(
                    fixture.fixture(),
                    fixture.fixture(),
                    fixture.fixture(),
                    fixture.fixture(),
                    fixture.fixture(),
                    fixture.fixture(),
                    preview = fixture.fixture(),
                    large = fixture.fixture(),
                ),
                PixabayItem(
                    fixture.fixture(),
                    fixture.fixture(),
                    fixture.fixture(),
                    fixture.fixture(),
                    fixture.fixture(),
                    fixture.fixture(),
                    preview = fixture.fixture(),
                    large = fixture.fixture(),
                ),
                PixabayItem(
                    expected.id,
                    fixture.fixture(),
                    fixture.fixture(),
                    fixture.fixture(),
                    fixture.fixture(),
                    fixture.fixture(),
                    preview = fixture.fixture(),
                    large = expected.url,
                ),
            ),
        )
        val dataSource: DataSourceContractMock = kmock()
        dataSource._resolveCat returns Ok(dto)

        val repository = Repository(
            dataSource,
            random,
        )
        // When
        val actual = repository.fetchCat()

        // Then
        actual mustBe expected
        (random as TestRandom).size mustBe dto.items.size
    }

    private class TestRandom(private val nextInt: Int) : Random() {
        var size by Delegates.notNull<Int>()

        override fun nextBits(bitCount: Int): Int {
            TODO("Not yet implemented")
        }

        override fun nextInt(until: Int): Int = nextInt.also {
            size = until
        }
    }
}
