/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.krump.bignumber

object BigUIntegerContract {
    internal interface BigUIntArithmetic {
        fun add(summand1: ByteArray, summand2: ByteArray): ByteArray
        fun subtract(minuend: ByteArray, subtrahend: ByteArray): ByteArray
        fun multiply(factor1: ByteArray, factor2: ByteArray): ByteArray
        fun divide(dividend: ByteArray, divisor: ByteArray): ByteArray
        fun remainder(number: ByteArray, modulus: ByteArray): ByteArray
        fun gcd(number: ByteArray, other: ByteArray): ByteArray
        fun shiftLeft(number: ByteArray, shifts: Long): ByteArray
        fun shiftRight(number: ByteArray, shifts: Long): ByteArray
        fun modPow(base: ByteArray, exponent: ByteArray, modulus: ByteArray): ByteArray
        fun modInverse(number: ByteArray, modulus: ByteArray): ByteArray
        fun intoString(number: ByteArray, radix: Int): String
        fun compare(number1: ByteArray, number2: ByteArray): Int
        // fun getProbablePrime(size: Int): String
    }

    interface BigUInteger {
        operator fun compareTo(other: BigUInteger): Int
        operator fun plus(summand: BigUInteger): BigUInteger
        operator fun minus(subtrahend: BigUInteger): BigUInteger
        operator fun times(factor: BigUInteger): BigUInteger
        operator fun div(divisor: BigUInteger): BigUInteger
        operator fun rem(modulus: BigUInteger): BigUInteger
        fun gcd(other: BigUInteger): BigUInteger
        infix fun shl(shifts: UInt): BigUInteger
        infix fun shr(shifts: UInt): BigUInteger
        fun modInverse(modulus: BigUInteger): BigUInteger
        fun modPow(
            exponent: BigUInteger,
            modulus: BigUInteger,
        ): BigUInteger

        @OptIn(ExperimentalUnsignedTypes::class)
        fun toUByteArray(): UByteArray
        fun toByteArray(): ByteArray
    }

    interface BigUIntegerFactory {
        fun from(number: String): BigUInteger
        fun from(number: UInt): BigUInteger

        @OptIn(ExperimentalUnsignedTypes::class)
        fun from(bytes: UByteArray): BigUInteger
        // fun getProbablePrime(size: Int): BigUInteger
    }

    internal const val SIGNED_NUMBER_ERROR = "Signed Numbers are forbidden!"
    internal const val PRIME_INIT_ERROR = "Prime size must be at least 2-bit."
}
