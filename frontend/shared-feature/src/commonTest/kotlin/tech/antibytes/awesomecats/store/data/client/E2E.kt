/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store.data.client

import kotlin.js.JsName
import kotlin.test.Ignore
import kotlin.test.Test
import tech.antibytes.util.test.coroutine.runBlockingTestWithTimeout
import tech.antibytes.util.test.isNot

@Ignore
class E2E {
    @Test
    @JsName("fn0")
    fun `It fetches Cats`() = runBlockingTestWithTimeout(5000) {
        // Given
        val client = CatClient.getInstance(
            logger = LoggerStub(),
            connection = { true },
        )

        // When
        val response = client.fetchCat()

        // Then
        response isNot null
    }
}
