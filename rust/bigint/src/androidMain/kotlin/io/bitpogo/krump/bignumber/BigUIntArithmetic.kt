/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.krump.bignumber

internal object BigUIntArithmetic : BigUIntegerContract.BigUIntArithmetic {
    init {
        System.loadLibrary("bigint_arithmetic")
    }

    external override fun add(summand1: ByteArray, summand2: ByteArray): ByteArray
    external override fun subtract(minuend: ByteArray, subtrahend: ByteArray): ByteArray
    external override fun multiply(factor1: ByteArray, factor2: ByteArray): ByteArray
    external override fun divide(dividend: ByteArray, divisor: ByteArray): ByteArray
    external override fun remainder(number: ByteArray, modulus: ByteArray): ByteArray
    external override fun gcd(number: ByteArray, other: ByteArray): ByteArray
    external override fun shiftLeft(number: ByteArray, shifts: Long): ByteArray
    external override fun shiftRight(number: ByteArray, shifts: Long): ByteArray
    external override fun modPow(base: ByteArray, exponent: ByteArray, modulus: ByteArray): ByteArray
    external override fun modInverse(number: ByteArray, modulus: ByteArray): ByteArray
    external override fun intoString(number: ByteArray, radix: Int): String
    external override fun compare(number1: ByteArray, number2: ByteArray): Int
    // external override fun getProbablePrime(size: Int): String
}
