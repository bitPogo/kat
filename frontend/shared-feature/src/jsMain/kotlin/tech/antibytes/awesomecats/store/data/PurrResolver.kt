/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store.data

import io.bitpogo.krump.bignumber.BigUIntegerContract.BigUIntegerFactory
import kotlin.math.absoluteValue
import kotlin.random.Random

actual typealias PurrMultiplier = BigUIntegerFactory

internal actual class PurrResolver actual constructor(
    random: Random,
    multiplier: PurrMultiplier
) : RepositoryContract.PurrResolver {
    private val random = random
    private val bigIntFactory = multiplier

    actual override suspend fun resolve(): String {
        val factor1 = bigIntFactory.from(
            random.nextInt().absoluteValue.toString()
        )
        val factor2 = bigIntFactory.from(
            random.nextInt().absoluteValue.toString()
        )

        return (factor1 * factor2).asString()
    }
}