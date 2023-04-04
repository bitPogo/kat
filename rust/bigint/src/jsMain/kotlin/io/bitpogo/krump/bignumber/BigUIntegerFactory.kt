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
import io.bitpogo.krump.bignumber.externals.BigInt
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.set

class BigUIntegerFactory internal constructor(
    private var rechenwerk: BigUIntArithmetic,
) : BigUIntegerContract.BigUIntegerFactory {
    constructor() : this(rechenwerk = BigUIntArithmetic)

    private fun validateString(number: String) {
        if (!number.first().isDigit()) {
            throw IllegalArgumentException(SIGNED_NUMBER_ERROR)
        }
    }

    private fun String.padStart(char: Char, predicate: String.() -> Boolean): String {
        return if (this.predicate()) {
            "$char$this"
        } else {
            this
        }
    }

    private fun BigInt.toUint8Array(): Uint8Array {
        val hex = toString(16).padStart('0') { (length and 1) == 1 }
        val bytes = Uint8Array(hex.length / 2)

        var hexIdx = 0
        (0 until bytes.length).forEach { idx ->
            bytes[idx] = hex.slice(hexIdx until hexIdx + 2).toInt(16).toByte()
            hexIdx += 2
        }

        return bytes
    }

    override fun from(number: String): BigUInteger {
        validateString(number)
        val bytes = BigInt(number).toUint8Array()
        return BigUInteger(
            bytes = bytes,
            rechenwerk = rechenwerk,
        )
    }

    override fun from(number: UInt): BigUInteger = from(number.toString(10))

    override fun from(bytes: UByteArray): BigUInteger {
        return BigUInteger(
            rechenwerk = rechenwerk,
            bytes = bytes.asByteArray(),
        )
    }

    /*private suspend fun guardPrime(
        size: Int,
        action: suspend (Int) -> BigUInteger,
    ): BigUInteger {
        return if (size < 2) {
            throw IllegalArgumentException(PRIME_INIT_ERROR)
        } else {
            action(size)
        }
    }

    override suspend fun getProbablePrime(size: Int): BigUInteger {
        return guardPrime(size) {
            from(rechenwerk.getProbablePrime(size))
        }
    }*/
}
