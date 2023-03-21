/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store.data

import kotlin.random.Random
import tech.antibytes.awesomecats.common.entity.Cat
import tech.antibytes.awesomecats.store.data.RepositoryContract.Client
import tech.antibytes.awesomecats.store.domain.RepositoryContract
import tech.antibytes.awesomecats.store.model.FrontendCat

internal class Repository(
    private val client: Client,
    private val random: Random,
) : RepositoryContract {
    private fun Cat.toFontEndCat(): FrontendCat {
        return FrontendCat(
            url = url,
            purrLevel = random.nextInt(),
        )
    }

    override suspend fun fetchFrontendCat(): FrontendCat {
        return try {
            Cat.fromString(client.fetchCat()).toFontEndCat()
        } catch (_: Throwable) {
            FrontendCat(
                "https://i.ytimg.com/vi/esxNJjOoTOQ/maxresdefault.jpg",
                0
            )
        }
    }
}
