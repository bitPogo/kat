/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store.data

import tech.antibytes.awesomecats.common.entity.Cat
import tech.antibytes.awesomecats.store.data.RepositoryContract.Client
import tech.antibytes.awesomecats.store.domain.RepositoryContract
import tech.antibytes.awesomecats.store.model.FrontendCat
import tech.antibytes.awesomecats.store.data.RepositoryContract.PurrResolver

internal class Repository(
    private val client: Client,
    private val purrResolver: PurrResolver
) : RepositoryContract {
    private suspend fun Cat.toFontEndCat(): FrontendCat {
        return FrontendCat(
            url = url,
            purrLevel = purrResolver.resolve(),
        )
    }

    override suspend fun fetchFrontendCat(): FrontendCat {
        return try {
            Cat.fromString(client.fetchCat()).toFontEndCat()
        } catch (_: Throwable) {
            FrontendCat(
                "https://i.ytimg.com/vi/esxNJjOoTOQ/maxresdefault.jpg",
                "0",
            )
        }
    }
}
