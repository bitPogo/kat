/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store.data

import kotlin.js.JsName
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlinx.coroutines.test.runTest
import tech.antibytes.awesomecats.store.model.FrontendCat
import tech.antibytes.awesomecats.viewmodel.kmock
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

@OptIn(KMockExperimental::class)
@KMock(
    RepositoryContract.Client::class,
)
class RepositorySpec {
    private val fixture = kotlinFixture()

    @Test
    @JsName("fn0")
    fun `It fulfils the RepositoryContract`() {
        Repository(kmock(), TestRandom(7)) fulfils RepositoryContract::class
    }

    @Test
    @JsName("fn1")
    fun `Given fetchFrontendCat and the Response is invalid it fails`() = runTest {
        // Given
        val response = "{}"
        val client: ClientMock = kmock()

        client._fetchCat returns response

        // Then
        assertFailsWith<Throwable> {
            // When
            Repository(client, TestRandom(23)).fetchFrontendCat()
        }
    }

    @Test
    @JsName("fn3")
    fun `Given fetchFrontendCat and the Response is returns a FrontEndCat`() = runTest {
        // Given
        val pur: Int = fixture.fixture()
        val url: Int = fixture.fixture()
        val random = TestRandom(pur)
        val response = "{\"url\":\"$url\", \"id\":\"${fixture.fixture<Int>()}\"}"
        val client: ClientMock = kmock()

        client._fetchCat returns response

        // When
        val actual = Repository(client, random).fetchFrontendCat()

        // Then
        actual mustBe FrontendCat(
            purrLevel = pur,
            url = url.toString(),
        )
    }

    private class TestRandom(private val nextInt: Int) : Random() {
        override fun nextBits(bitCount: Int): Int {
            TODO("Not yet implemented")
        }

        override fun nextInt(): Int = nextInt
    }
}
