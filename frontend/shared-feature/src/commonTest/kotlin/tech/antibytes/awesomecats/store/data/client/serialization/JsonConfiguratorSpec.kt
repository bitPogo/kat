/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store.data.client.serialization

import kotlin.js.JsName
import kotlin.test.Test
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

class JsonConfiguratorSpec {
    @Test
    @JsName("fn0")
    fun `It fulfils JsonConfigurator`() {
        JsonConfigurator() fulfils JsonConfiguratorContract::class
    }

    @Test
    @JsName("fn1")
    fun `Given configure is called with a JsonBuilder it returns a JsonBuilder`() {
        // Given
        var builder: JsonBuilder? = null
        Json { builder = this }

        // When
        val result: Any = JsonConfigurator().configure(builder!!)

        // Then
        result fulfils JsonBuilder::class
    }

    @Test
    @JsName("fn2")
    fun `Given configure is called it configures the resulting Json Serializer`() {
        // Given
        var builder: JsonBuilder? = null
        Json { builder = this }

        // When
        val result = JsonConfigurator().configure(builder!!)

        // Then
        result.isLenient mustBe true
        result.ignoreUnknownKeys mustBe true
        result.allowSpecialFloatingPointValues mustBe true
        result.useArrayPolymorphism mustBe false
    }
}
