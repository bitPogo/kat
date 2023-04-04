/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store.data

import kotlin.js.JsName
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import tech.antibytes.awesomecats.store.model.FrontendCat
import tech.antibytes.awesomecats.viewmodel.kmock
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.awesomecats.store.data.RepositoryContract.Client
import tech.antibytes.awesomecats.store.data.RepositoryContract.PurrResolver

@OptIn(KMockExperimental::class)
@KMock(
    Client::class,
    PurrResolver::class,
)
class RepositorySpec {
    private val fixture = kotlinFixture()

    @Test
    @JsName("fn0")
    fun `It fulfils the RepositoryContract`() {
        Repository(kmock(), kmock()) fulfils RepositoryContract::class
    }

    @Test
    @JsName("fn1")
    fun `Given fetchFrontendCat and the Response is invalid it returns DefaultCat`() = runTest {
        // Given
        val response = "{}"
        val client: ClientMock = kmock()
        val resolver: PurrResolverMock = kmock()

        client._fetchCat returns response

        // When
        val actual = Repository(client, resolver).fetchFrontendCat()

        // Then
        actual mustBe FrontendCat(
            "https://i.ytimg.com/vi/esxNJjOoTOQ/maxresdefault.jpg",
            "0",
        )
    }

    @Test
    @JsName("fn3")
    fun `Given fetchFrontendCat and the Response is returns a FrontEndCat`() = runTest {
        // Given
        val pur: Int = fixture.fixture()
        val url: Int = fixture.fixture()
        val resolver: PurrResolverMock = kmock()
        val response = "{\"url\":\"$url\", \"id\":\"${fixture.fixture<Int>()}\"}"
        val client: ClientMock = kmock()

        client._fetchCat returns response
        resolver._resolve returns pur.toString()

        // When
        val actual = Repository(client, resolver).fetchFrontendCat()

        // Then
        actual mustBe FrontendCat(
            purrLevel = pur.toString(),
            url = url.toString(),
        )
    }
}
