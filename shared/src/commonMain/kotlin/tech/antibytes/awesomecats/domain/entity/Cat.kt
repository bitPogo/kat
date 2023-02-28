/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.domain.entity

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlin.jvm.JvmStatic

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
