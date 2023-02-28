/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.backend.pixabay.data.datsource

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.unwrap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import tech.antibytes.awesomecats.backend.kmock
import tech.antibytes.kfixture.fixture
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.pixabay.sdk.ClientContract
import tech.antibytes.pixabay.sdk.model.PixabayItem
import tech.antibytes.pixabay.sdk.model.PixabayResponse
import tech.antibytes.awesomecats.backend.pixabay.data.repository.DataSourceContract
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.random.Random
import tech.antibytes.kmock.verification.assertProxy
import tech.antibytes.pixabay.sdk.ClientMock
import tech.antibytes.kfixture.kotlinFixture

@OptIn(KMockExperimental::class)
@KMock(
    ClientContract.Client::class
)
class DatasourceTest {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils DataSourceContract`() {
        DataSource(kmock(), TestRandom(1u)) fulfils DataSourceContract::class
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Given resolveCat is called it resolves a Cat`() = runTest {
        // Given
        val random = TestRandom(fixture.fixture())
        val expected = PixabayResponse(
            total = 223,
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
                    fixture.fixture(),
                    fixture.fixture(),
                    fixture.fixture(),
                    fixture.fixture(),
                    fixture.fixture(),
                    fixture.fixture(),
                    preview = fixture.fixture(),
                    large = fixture.fixture(),
                ),
            ),
        )
        val client: ClientMock = kmock()
        client._fetchImages returns Ok(expected)

        // When
        val actual = DataSource(client, random).resolveCat().unwrap()

        // Then
        actual mustBe expected
        random.range mustBe 1..3
        assertProxy {
            client._fetchImages.hasBeenCalledWith("cats", random.nextUInt.toUShort())
        }
    }

    private class TestRandom(val nextUInt: UInt) : Random() {
        lateinit var range: IntRange

        override fun nextBits(bitCount: Int): Int {
            TODO("Not yet implemented")
        }

        override fun nextInt(from: Int, until: Int): Int = nextUInt.toInt().also {
            range = from..until
        }
    }
}
