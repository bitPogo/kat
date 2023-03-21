/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.backend.pixabay.domain.usecase

import kotlinx.coroutines.flow.Flow
import tech.antibytes.awesomecats.common.entity.Cat

interface UsecaseContract {
    fun resolveCat(): Flow<Cat>
}
