/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.krump.bignumber

import org.khronos.webgl.Uint8Array

object BigUIntegerContract {
    internal interface BigUIntArithmetic {
        suspend fun add(summand1: Uint8Array, summand2: Uint8Array): Uint8Array
        suspend fun subtract(minuend: Uint8Array, subtrahend: Uint8Array): Uint8Array
        suspend fun multiply(factor1: Uint8Array, factor2: Uint8Array): Uint8Array
        suspend fun divide(dividend: Uint8Array, divisor: Uint8Array): Uint8Array
        suspend fun remainder(number: Uint8Array, modulus: Uint8Array): Uint8Array
        suspend fun gcd(number: Uint8Array, other: Uint8Array): Uint8Array
        suspend fun shiftLeft(number: Uint8Array, shifts: Long): Uint8Array
        suspend fun shiftRight(number: Uint8Array, shifts: Long): Uint8Array
        suspend fun modPow(base: Uint8Array, exponent: Uint8Array, modulus: Uint8Array): Uint8Array
        suspend fun modInverse(number: Uint8Array, modulus: Uint8Array): Uint8Array
        suspend fun intoString(number: Uint8Array, radix: Int): String
        suspend fun compare(number1: Uint8Array, number2: Uint8Array): Int
        suspend fun getProbablePrime(size: Int): String
    }

    interface BigUInteger {
        suspend operator fun compareTo(other: BigUInteger): Int
        suspend operator fun plus(summand: BigUInteger): BigUInteger
        suspend operator fun minus(subtrahend: BigUInteger): BigUInteger
        suspend operator fun times(factor: BigUInteger): BigUInteger
        suspend operator fun div(divisor: BigUInteger): BigUInteger
        suspend operator fun rem(modulus: BigUInteger): BigUInteger
        suspend fun gcd(other: BigUInteger): BigUInteger
        suspend infix fun shl(shifts: UInt): BigUInteger
        suspend infix fun shr(shifts: UInt): BigUInteger
        suspend fun modInverse(modulus: BigUInteger): BigUInteger
        suspend fun modPow(
            exponent: BigUInteger,
            modulus: BigUInteger,
        ): BigUInteger

        suspend fun asString(): String
        suspend fun equalsTo(other: Any?): Boolean
        fun toUint8Array(): Uint8Array
        fun toUByteArray(): UByteArray
        fun toByteArray(): ByteArray
    }

    interface BigUIntegerFactory {
        fun from(number: String): BigUInteger
        fun from(number: UInt): BigUInteger

        @OptIn(ExperimentalUnsignedTypes::class)
        fun from(bytes: UByteArray): BigUInteger
        // suspend fun getProbablePrime(size: Int): BigUInteger
    }

    internal const val SIGNED_NUMBER_ERROR = "Signed Numbers are forbidden!"
    internal const val PRIME_INIT_ERROR = "Prime size must be at least 2-bit."
}
