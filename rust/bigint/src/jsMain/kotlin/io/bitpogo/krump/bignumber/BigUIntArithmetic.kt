package io.bitpogo.krump.bignumber

import io.bitpogo.krump.bignumber.externals.add as _add
import io.bitpogo.krump.bignumber.externals.compare as _compare
import io.bitpogo.krump.bignumber.externals.divide as _divide
import io.bitpogo.krump.bignumber.externals.gcd as _gcd
import io.bitpogo.krump.bignumber.externals.getProbablePrime as _getProbablePrime
import io.bitpogo.krump.bignumber.externals.init
import io.bitpogo.krump.bignumber.externals.intoString as _intoString
import io.bitpogo.krump.bignumber.externals.modInverse as _modInverse
import io.bitpogo.krump.bignumber.externals.modPow as _modPow
import io.bitpogo.krump.bignumber.externals.multiply as _multiply
import io.bitpogo.krump.bignumber.externals.remainder as _remainder
import io.bitpogo.krump.bignumber.externals.shiftLeft as _shiftLeft
import io.bitpogo.krump.bignumber.externals.shiftRight as _shiftRight
import io.bitpogo.krump.bignumber.externals.subtract as _subtract
import kotlinx.coroutines.await
import org.khronos.webgl.Uint8Array

internal object BigUIntArithmetic : BigUIntegerContract.BigUIntArithmetic {
    private suspend fun <T : Any> runOperation(
        operation: () -> T,
    ): T = init(
        "bigint_arithmetic_bg.wasm",
    ).then {
        operation()
    }.await()

    override suspend fun add(
        summand1: Uint8Array,
        summand2: Uint8Array,
    ): Uint8Array = runOperation { _add(summand1, summand2) }

    override suspend fun subtract(
        minuend: Uint8Array,
        subtrahend: Uint8Array,
    ): Uint8Array = runOperation { _subtract(minuend, subtrahend) }

    override suspend fun multiply(
        factor1: Uint8Array,
        factor2: Uint8Array,
    ): Uint8Array = runOperation { _multiply(factor1, factor2) }

    override suspend fun divide(
        dividend: Uint8Array,
        divisor: Uint8Array,
    ): Uint8Array = runOperation { _divide(dividend, divisor) }

    override suspend fun remainder(
        number: Uint8Array,
        modulus: Uint8Array,
    ): Uint8Array = runOperation { _remainder(number, modulus) }

    override suspend fun gcd(
        number: Uint8Array,
        other: Uint8Array,
    ): Uint8Array = runOperation { _gcd(number, other) }

    override suspend fun shiftLeft(
        number: Uint8Array,
        shifts: Long,
    ): Uint8Array = runOperation { _shiftLeft(number, shifts.toString()) }

    override suspend fun shiftRight(
        number: Uint8Array,
        shifts: Long,
    ): Uint8Array = runOperation { _shiftRight(number, shifts.toString()) }

    override suspend fun modPow(
        base: Uint8Array,
        exponent: Uint8Array,
        modulus: Uint8Array,
    ): Uint8Array = runOperation { _modPow(base, exponent, modulus) }

    override suspend fun modInverse(
        number: Uint8Array,
        modulus: Uint8Array,
    ): Uint8Array = runOperation { _modInverse(number, modulus) }

    override suspend fun intoString(
        number: Uint8Array,
        radix: Int,
    ): String = runOperation { _intoString(number, radix) }

    override suspend fun compare(
        number1: Uint8Array,
        number2: Uint8Array,
    ): Int = runOperation { _compare(number1, number2) }.toInt()

    override suspend fun getProbablePrime(
        size: Int,
    ): String = runOperation { _getProbablePrime(size) }
}
