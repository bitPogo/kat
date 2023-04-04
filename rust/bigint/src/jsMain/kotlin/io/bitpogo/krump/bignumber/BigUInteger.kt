/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.krump.bignumber

import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get

private fun ByteArray.asUint8Array(): Uint8Array = Uint8Array(this.toTypedArray())
private fun Uint8Array.asByteArray(): ByteArray {
    val bytes = ByteArray(length)
    (0 until length).forEach { idx ->
        bytes[idx] = this[idx]
    }

    return bytes
}

internal class BigUInteger internal constructor(
    private val bytes: Uint8Array,
    private val rechenwerk: BigUIntegerContract.BigUIntArithmetic,
) : BigUIntegerContract.BigUInteger {
    internal constructor(
        rechenwerk: BigUIntegerContract.BigUIntArithmetic,
        bytes: ByteArray,
    ) : this(bytes.asUint8Array(), rechenwerk)

    override suspend fun compareTo(
        other: BigUIntegerContract.BigUInteger,
    ): Int = rechenwerk.compare(bytes, other.toUint8Array())

    private suspend fun wrapUpOperation(
        operation: suspend () -> Uint8Array,
    ): BigUIntegerContract.BigUInteger {
        val result = operation()

        return BigUInteger(result, rechenwerk)
    }

    override suspend fun plus(summand: BigUIntegerContract.BigUInteger): BigUIntegerContract.BigUInteger {
        return wrapUpOperation {
            rechenwerk.add(bytes, summand.toUint8Array())
        }
    }

    override suspend fun minus(subtrahend: BigUIntegerContract.BigUInteger): BigUIntegerContract.BigUInteger {
        return if (this < subtrahend) {
            throw IllegalArgumentException(MINUEND_TOO_SMALL)
        } else {
            wrapUpOperation {
                rechenwerk.subtract(bytes, subtrahend.toUint8Array())
            }
        }
    }

    override suspend fun times(factor: BigUIntegerContract.BigUInteger): BigUIntegerContract.BigUInteger {
        return wrapUpOperation {
            rechenwerk.multiply(bytes, factor.toUint8Array())
        }
    }

    override suspend fun div(divisor: BigUIntegerContract.BigUInteger): BigUIntegerContract.BigUInteger {
        return wrapUpOperation {
            rechenwerk.divide(bytes, divisor.toUint8Array())
        }
    }

    override suspend fun rem(modulus: BigUIntegerContract.BigUInteger): BigUIntegerContract.BigUInteger {
        return wrapUpOperation {
            rechenwerk.remainder(bytes, modulus.toUint8Array())
        }
    }

    override suspend fun gcd(other: BigUIntegerContract.BigUInteger): BigUIntegerContract.BigUInteger {
        return wrapUpOperation {
            rechenwerk.gcd(bytes, other.toUint8Array())
        }
    }

    override suspend fun shl(shifts: UInt): BigUIntegerContract.BigUInteger {
        return wrapUpOperation {
            rechenwerk.shiftLeft(bytes, shifts.toLong())
        }
    }

    override suspend fun shr(shifts: UInt): BigUIntegerContract.BigUInteger {
        return wrapUpOperation {
            rechenwerk.shiftRight(bytes, shifts.toLong())
        }
    }

    override suspend fun modInverse(modulus: BigUIntegerContract.BigUInteger): BigUIntegerContract.BigUInteger {
        val result = rechenwerk.modInverse(bytes, modulus.toUint8Array())

        return if (result.length == 0) {
            throw RuntimeException(NO_MULTIPLICATIVE_INVERSE)
        } else {
            BigUInteger(result, rechenwerk)
        }
    }

    override suspend fun modPow(
        exponent: BigUIntegerContract.BigUInteger,
        modulus: BigUIntegerContract.BigUInteger,
    ): BigUIntegerContract.BigUInteger {
        return wrapUpOperation {
            rechenwerk.modPow(
                base = bytes,
                exponent = exponent.toUint8Array(),
                modulus = modulus.toUint8Array(),
            )
        }
    }

    override fun equals(other: Any?): Boolean = this === other

    override suspend fun equalsTo(other: Any?): Boolean {
        val ownValue = asString()
        return if (other is BigUIntegerContract.BigUInteger) {
            ownValue == other.asString()
        } else {
            ownValue == other.toString()
        }
    }

    override suspend fun asString(): String = rechenwerk.intoString(bytes, 10)

    override fun toUint8Array(): Uint8Array = this.bytes

    override fun toUByteArray(): UByteArray = bytes.asByteArray().asUByteArray()

    override fun toByteArray(): ByteArray = bytes.asByteArray()

    private companion object {
        const val MINUEND_TOO_SMALL = "The minuend must be greater than the subtrahend!"
        const val NO_MULTIPLICATIVE_INVERSE = "The multiplicative inverse does not exists!"
    }
}
