package tech.antibytes.lib

import kotlin.js.JsName
import kotlin.test.Test
import tech.antibytes.util.test.fulfils

class ThingSpec {
    @Test
    @JsName("fn0")
    fun `A Thing exists`() {
        Thing() fulfils Any::class
    }
}
