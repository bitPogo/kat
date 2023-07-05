/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.krump.bignumber

import kotlinx.cinterop.*
import io.bitpogo.krump.rechenwerk.*
import kotlin.text.encodeToByteArray

internal object BigUIntArithmetic : BigUIntegerContract.BigUIntArithmetic {
    private fun CPointer<ByteVar>.toByteArrayAndFree(): ByteArray { 
        return toKString().encodeToByteArray().also {
            freeString(this)
        }
    }
    
    override fun add(summand1: ByteArray, summand2: ByteArray): ByteArray {
        return addNative(
            summand1.decodeToString(),
            summand2.decodeToString()
        )!!.toByteArrayAndFree()
    }

    override fun subtract(minuend: ByteArray, subtrahend: ByteArray): ByteArray {
        return subtractNative(
            minuend.decodeToString(),
            subtrahend.decodeToString()
        )!!.toByteArrayAndFree()
    }

    override fun multiply(factor1: ByteArray, factor2: ByteArray): ByteArray {
        return multiplyNative(
            factor1.decodeToString(),
            factor1.decodeToString()
        )!!.toByteArrayAndFree()
    }

    override fun divide(dividend: ByteArray, divisor: ByteArray): ByteArray {
        return divideNative(
            dividend.decodeToString(),
            divisor.decodeToString()
        )!!.toByteArrayAndFree()
    }

    override fun remainder(number: ByteArray, modulus: ByteArray): ByteArray {
        return remainderNative(
            number.decodeToString(),
            modulus.decodeToString()
        )!!.toByteArrayAndFree()
    }

    override fun gcd(number: ByteArray, other: ByteArray): ByteArray {
        return gcdNative(
            number.decodeToString(),
            other.decodeToString()
        )!!.toByteArrayAndFree()
    }

    override fun shiftLeft(number: ByteArray, shifts: Long): ByteArray {
        return shiftLeftNative(
            number.decodeToString(),
            shifts
        )!!.toByteArrayAndFree()
    }

    override fun shiftRight(number: ByteArray, shifts: Long): ByteArray {
        return shiftRightNative(
            number.decodeToString(),
            shifts
        )!!.toByteArrayAndFree()
    }

    override fun modPow(base: ByteArray, exponent: ByteArray, modulus: ByteArray): ByteArray {
        return modPowNative(
            base.decodeToString(),
            exponent.decodeToString(),
            modulus.decodeToString(),
        )!!.toByteArrayAndFree()
    }

    override fun modInverse(number: ByteArray, modulus: ByteArray): ByteArray {
        return modInverseNative(
            number.decodeToString(),
            modulus.decodeToString(),
        )!!.toByteArrayAndFree()
    }

    override fun intoString(number: ByteArray, radix: Int): String {
        val result = intoStringNative(
            number.decodeToString(),
            radix,
        )!!

        return result.toKString().also {
            freeString(result)
        }
    }

    override fun compare(number1: ByteArray, number2: ByteArray): Int {
        return compareNative(
            number1.decodeToString(),
            number2.decodeToString(),
        )
    }

    /*override fun getProbablePrime(size: Int): String {
        return getProbablePrimeNative(size)!!

        return result.toKString().also {
            freeString(result)
        }
    }*/
}
