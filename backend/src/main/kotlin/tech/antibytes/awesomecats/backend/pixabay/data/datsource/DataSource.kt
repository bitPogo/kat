/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.backend.pixabay.data.datsource

import com.github.michaelbull.result.Result
import kotlin.random.Random
import kotlin.random.nextUInt
import tech.antibytes.awesomecats.backend.pixabay.data.repository.DataSourceContract
import tech.antibytes.pixabay.sdk.ClientContract.Client
import tech.antibytes.pixabay.sdk.error.PixabayClientError
import tech.antibytes.pixabay.sdk.model.PixabayResponse

internal class DataSource(
    private val client: Client,
    private val random: Random,
) : DataSourceContract {
    override suspend fun resolveCat(): Result<PixabayResponse, PixabayClientError> {
        return client.fetchImages(
            "cats",
            random.nextUInt(1u until 3u).toUShort(),
        )
    }
}
