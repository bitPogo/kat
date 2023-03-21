/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.backend.pixabay.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import tech.antibytes.awesomecats.common.entity.Cat

internal class Usecase(
    private val repository: RepositoryContract,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : UsecaseContract {
    override fun resolveCat(): Flow<Cat> {
        return flow {
            emit(repository.fetchCat())
        }.flowOn(dispatcher)
    }
}
