/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store.data

import tech.antibytes.awesomecats.store.data.RepositoryContract.PurrResolver
import kotlin.random.Random

expect interface PurrMultiplier

internal expect class PurrResolver constructor(
    random: Random,
    multiplier: PurrMultiplier
) : PurrResolver {
    override suspend fun resolve(): String
}