/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.krump.bignumber

import io.bitpogo.krump.bignumber.BigUIntegerContract.BigUIntArithmetic
import io.bitpogo.krump.bignumber.BigUIntegerContract.PRIME_INIT_ERROR
import io.bitpogo.krump.bignumber.BigUIntegerContract.SIGNED_NUMBER_ERROR

actual class BigUIntegerFactory internal actual constructor(
    private var rechenwerk: BigUIntArithmetic,
) : BigUIntegerContract.BigUIntegerFactory {
    actual constructor() : this(rechenwerk = BigUIntArithmetic)

    private fun validateString(number: String) {
        if (!number.first().isDigit()) {
            throw IllegalArgumentException(SIGNED_NUMBER_ERROR)
        }
    }

    actual override fun from(number: String): BigUIntegerContract.BigUInteger {
        validateString(number)
        return BigUInteger(
            rechenwerk = rechenwerk,
            bytes = number.encodeToByteArray().toUByteArray(),
        )
    }

    actual override fun from(number: UInt): BigUIntegerContract.BigUInteger = from(number.toString())

    @OptIn(ExperimentalUnsignedTypes::class)
    actual override fun from(
        bytes: UByteArray,
    ): BigUIntegerContract.BigUInteger {
        return BigUInteger(
            rechenwerk = rechenwerk,
            bytes = bytes,
        )
    }

    private fun guardPrime(
        size: Int,
        action: (Int) -> BigUIntegerContract.BigUInteger,
    ): BigUIntegerContract.BigUInteger {
        return if (size < 2) {
            throw IllegalArgumentException(PRIME_INIT_ERROR)
        } else {
            action(size)
        }
    }

    /*actual override fun getProbablePrime(size: Int): BigUIntegerContract.BigUInteger {
        return guardPrime(size) {
            from(rechenwerk.getProbablePrime(size))
        }
    }*/
}
