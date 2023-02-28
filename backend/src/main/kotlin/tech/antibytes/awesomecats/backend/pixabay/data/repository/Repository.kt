/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.backend.pixabay.data.repository

import com.github.michaelbull.result.unwrap
import kotlin.random.Random
import tech.antibytes.pixabay.sdk.model.PixabayItem
import tech.antibytes.awesomecats.backend.pixabay.domain.usecase.RepositoryContract
import tech.antibytes.awesomecats.domain.entity.Cat

internal class Repository(
    private val source: DataSourceContract,
    private val random: Random,
) : RepositoryContract {
    private fun PixabayItem.toEntity(): Cat {
        return Cat(
            id = id,
            url = large,
        )
    }

    override suspend fun fetchCat(): Cat {
        val dto = source.resolveCat().unwrap()

        return dto.items[random.nextInt(dto.items.size)].toEntity()
    }
}
