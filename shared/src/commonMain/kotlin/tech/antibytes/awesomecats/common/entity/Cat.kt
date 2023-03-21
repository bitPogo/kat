/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.common.entity

import kotlin.jvm.JvmStatic
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Cat(
    val id: Long,
    val url: String,
) {
    override fun toString(): String = Json.encodeToString(this)

    companion object {
        @JvmStatic
        fun fromString(serializedCat: String): Cat = Json.decodeFromString(serializedCat)
    }
}
