/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.krump.bignumber

import io.bitpogo.krump.bignumber.BigUIntegerContract.BigUIntArithmetic
import io.bitpogo.krump.bignumber.BigUIntegerContract.BigUInteger
import io.bitpogo.krump.bignumber.BigUIntegerContract.PRIME_INIT_ERROR
import io.bitpogo.krump.bignumber.BigUIntegerContract.SIGNED_NUMBER_ERROR
import java.math.BigInteger
import java.security.SecureRandom

actual class BigUIntegerFactory internal actual constructor(
    private var rechenwerk: BigUIntArithmetic,
) : BigUIntegerContract.BigUIntegerFactory {
    actual constructor() : this(rechenwerk = BigUIntArithmetic)

    private val random = SecureRandom()

    private fun validateString(number: String) {
        if (!number.first().isDigit()) {
            throw IllegalArgumentException(SIGNED_NUMBER_ERROR)
        }
    }

    actual override fun from(number: String): BigUInteger {
        validateString(number)
        val bigInt = BigInteger(number, 10)

        return BigUInteger(
            rechenwerk = rechenwerk,
            bytes = bigInt.toByteArray().asUByteArray(),
        )
    }

    actual override fun from(number: UInt): BigUInteger = from(number.toString())

    @OptIn(ExperimentalUnsignedTypes::class)
    actual override fun from(
        bytes: UByteArray,
    ): BigUInteger {
        return BigUInteger(
            rechenwerk = rechenwerk,
            bytes = bytes,
        )
    }

    /*private fun guardPrime(
        size: Int,
        action: (Int) -> BigUInteger,
    ): BigUInteger {
        return if (size < 2) {
            throw IllegalArgumentException(PRIME_INIT_ERROR)
        } else {
            action(size)
        }
    }

    actual override fun getProbablePrime(size: Int): BigUInteger {
        return guardPrime(size) {
            val bigInt = BigInteger.probablePrime(size, random)
            val bytes = bigInt.toByteArray().asUByteArray()

            BigUInteger(
                rechenwerk = rechenwerk,
                bytes = bytes,
            )
        }
    }*/
}
