/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store.domain

import tech.antibytes.awesomecats.store.UseCaseContract
import tech.antibytes.awesomecats.store.model.FrontendCat

internal class UseCase(
    private val repository: RepositoryContract,
) : UseCaseContract {
    override suspend fun findACat(): FrontendCat = repository.fetchFrontendCat()
}
