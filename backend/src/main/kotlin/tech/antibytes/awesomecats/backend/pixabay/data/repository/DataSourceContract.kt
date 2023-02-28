/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.backend.pixabay.data.repository

import com.github.michaelbull.result.Result
import tech.antibytes.pixabay.sdk.error.PixabayClientError
import tech.antibytes.pixabay.sdk.model.PixabayResponse

interface DataSourceContract {
    suspend fun resolveCat(): Result<PixabayResponse, PixabayClientError>
}
