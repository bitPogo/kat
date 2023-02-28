/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.backend.pixabay.domain.usecase

import tech.antibytes.awesomecats.domain.entity.Cat

interface RepositoryContract {
    suspend fun fetchCat(): Cat
}
