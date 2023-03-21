/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.backend

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import tech.antibytes.util.test.mustBe

// TODO Resolve that Integration Test
class ServerSpec {
    @Test
    @Disabled
    fun `Given resolvePlainCat is called it resolves Cats`() {
        // Given
        testApplication {
            application {
                addCORS()
                route()
            }

            // When
            val response = client.get("/")

            // Then
            response.headers.contains("Access-Control-Allow-Origin", "*") mustBe true
            response.bodyAsText().contains("url") mustBe true
        }
    }
}
