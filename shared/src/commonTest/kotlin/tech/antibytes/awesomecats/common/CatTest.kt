/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.common

import kotlin.js.JsName
import kotlin.test.Test
import tech.antibytes.awesomecats.common.entity.Cat
import tech.antibytes.util.test.mustBe

class CatTest {
    @Test
    @JsName("fn0")
    fun `It is serializable`() {
        Cat(
            id = 1234,
            url = "test",
        ).toString() mustBe "{\"id\":1234,\"url\":\"test\"}"
    }

    @Test
    @JsName("fn1")
    fun `It is deserializable`() {
        Cat.fromString("{\"id\":1234,\"url\":\"test\"}") mustBe Cat(id = 1234, url = "test")
    }
}
