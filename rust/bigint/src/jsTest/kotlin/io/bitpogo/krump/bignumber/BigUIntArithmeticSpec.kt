package io.bitpogo.krump.bignumber

import io.bitpogo.krump.bignumber.externals.BigInt
import kotlin.math.ceil
import kotlin.test.Test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import tech.antibytes.util.test.mustBe

@OptIn(ExperimentalUnsignedTypes::class, ExperimentalCoroutinesApi::class)
class BigUIntArithmeticSpec {
    private fun ByteArray.asUint8Array(): Uint8Array = Uint8Array(this.toTypedArray())
    private fun Uint8Array.asBigInt(): BigInt {
        val inHex = mutableListOf<String>()
        (0 until length).forEach { idx ->
            @Suppress("UNUSED_VARIABLE")
            val byte = this[idx]

            val hex = js("byte.toString(16);") as String
            if ((hex.length and 1) == 1) {
                inHex.add("0$hex")
            } else {
                inHex.add(hex)
            }
        }

        return BigInt("0x${inHex.joinToString("")}")
    }

    @Test
    fun Given_two_numbers_it_adds_them() = runTest {
        // Given
        val value = UByteArray(2)
        value[0] = 1u
        value[1] = 42u

        val summand1 = value.asByteArray().asUint8Array()
        val summand2 = value.asByteArray().asUint8Array()

        // When
        val actual = BigUIntArithmetic.add(summand1, summand2)

        // Then
        actual.asBigInt().toString() mustBe "596"
    }

    @Test
    fun Given_two_numbers_it_subtracts_them() = runTest {
        // Given
        val value = UByteArray(2)
        value[0] = 1u
        value[1] = 42u

        val minuend = value.asByteArray().asUint8Array()
        val subtrahend = value.asByteArray().asUint8Array()

        // When
        val actual = BigUIntArithmetic.subtract(minuend, subtrahend)

        // Then
        actual.asBigInt().toString() mustBe "0"
    }

    @Test
    fun Given_two_numbers_it_multiplies_them() = runTest {
        // Given
        val value = UByteArray(2)
        value[0] = 1u
        value[1] = 42u

        val factor1 = value.asByteArray().asUint8Array()
        val factor2 = value.asByteArray().asUint8Array()

        // When
        val actual = BigUIntArithmetic.multiply(factor1, factor2)

        // Then
        actual.asBigInt().toString() mustBe "88804"
    }

    @Test
    fun Given_two_numbers_it_diviedes_them() = runTest {
        // Given
        val valueDividend = UByteArray(3)
        valueDividend[0] = 1u
        valueDividend[1] = 90u
        valueDividend[2] = 228u

        val valueDivisor = UByteArray(2)
        valueDivisor[0] = 1u
        valueDivisor[1] = 42u

        val dividend = valueDividend.asByteArray().asUint8Array()
        val divisor = valueDivisor.asByteArray().asUint8Array()

        // When
        val actual = BigUIntArithmetic.divide(dividend, divisor)

        // Then
        actual.asBigInt().toString() mustBe "298"
    }

    @Test
    fun Given_two_numbers_it_detetermines_the_remainder() = runTest {
        // Given
        val valueNumber = UByteArray(3)
        valueNumber[0] = 1u
        valueNumber[1] = 90u
        valueNumber[2] = 228u

        val valueModulus = UByteArray(2)
        valueModulus[0] = 1u
        valueModulus[1] = 42u

        val number = valueNumber.asByteArray().asUint8Array()
        val modulus = valueModulus.asByteArray().asUint8Array()

        // When
        val actual = BigUIntArithmetic.remainder(number, modulus)

        // Then
        actual.asBigInt().toString() mustBe "0"
    }

    @Test
    fun Given_two_numbers_it_detetermines_the_greatest_common_divisor() = runTest {
        // Given
        val valueNumber = UByteArray(3)
        valueNumber[0] = 1u
        valueNumber[1] = 90u
        valueNumber[2] = 228u

        val valueOther = UByteArray(2)
        valueOther[0] = 1u
        valueOther[1] = 42u

        val number = valueNumber.asByteArray().asUint8Array()
        val other = valueOther.asByteArray().asUint8Array()

        // When
        val actual = BigUIntArithmetic.gcd(number, other)

        // Then
        actual.asBigInt().toString() mustBe "298"
    }

    @Test
    fun Given_two_numbers_it_left_shifts() = runTest {
        // Given
        val valueNumber = UByteArray(3)
        valueNumber[0] = 1u
        valueNumber[1] = 90u
        valueNumber[2] = 228u

        val number = valueNumber.asByteArray().asUint8Array()
        val other = 298L

        // When
        val actual = BigUIntArithmetic.shiftLeft(number, other)

        // Then
        actual.asBigInt().toString() mustBe "45224235710601925601245762728376604553503649807777450700372954116116619236045838213977605144576"
    }

    @Test
    fun Given_two_numbers_it_right_shifts() = runTest {
        // Given
        val valueNumber = UByteArray(3)
        valueNumber[0] = 1u
        valueNumber[1] = 90u
        valueNumber[2] = 228u

        val number = valueNumber.asByteArray().asUint8Array()
        val other = 10L

        // When
        val actual = BigUIntArithmetic.shiftRight(number, other)

        // Then
        actual.asBigInt().toString() mustBe "86"
    }

    @Test
    fun Given_three_numbers_it_pows_the_base_with_exponent_while_calculating_the_remainder() = runTest {
        // Given
        val valueBase = UByteArray(3)
        valueBase[0] = 1u
        valueBase[1] = 90u
        valueBase[2] = 228u

        val valueExponent = UByteArray(2)
        valueExponent[0] = 1u
        valueExponent[1] = 42u

        val valueModulus = UByteArray(2)
        valueModulus[0] = 1u
        valueModulus[1] = 23u

        val base = valueBase.asByteArray().asUint8Array()
        val exponent = valueExponent.asByteArray().asUint8Array()
        val modulus = valueModulus.asByteArray().asUint8Array()

        // When
        val actual = BigUIntArithmetic.modPow(base, exponent, modulus)

        // Then
        actual.asBigInt().toString() mustBe "10"
    }

    @Test
    fun Given_two_numbers_it_determines_the_multiplicative_inverse() = runTest {
        // Given
        val valueNumber = UByteArray(3)
        valueNumber[0] = 1u
        valueNumber[1] = 90u
        valueNumber[2] = 228u

        val valueModulus = UByteArray(2)
        valueModulus[0] = 1u
        valueModulus[1] = 42u

        val number = valueNumber.asByteArray().asUint8Array()
        val modulus = valueModulus.asByteArray().asUint8Array()

        // When
        val actual = BigUIntArithmetic.modInverse(number, modulus)

        // Then
        actual.length mustBe 0
    }

    @Test
    fun Given_a_number_it_transforms_it_into_a_string() = runTest {
        // Given
        val valueToString = UByteArray(3)
        valueToString[0] = 1u
        valueToString[1] = 90u
        valueToString[2] = 228u

        val number = valueToString.asByteArray().asUint8Array()

        // When
        val actual = BigUIntArithmetic.intoString(number, 10)

        // Then
        actual mustBe "88804"
    }

    @Test
    fun Given_tow_numbers_it_compares_them() = runTest {
        // Given
        val valueToString = UByteArray(3)
        valueToString[0] = 1u
        valueToString[1] = 90u
        valueToString[2] = 228u

        val number = valueToString.asByteArray().asUint8Array()

        // When
        val actual = BigUIntArithmetic.compare(number, number)

        // Then
        actual mustBe 0
    }

    @Test
    fun Given_getProbablePrime_it_returns_a_prime() = runTest {
        // Given
        val size = 42

        // When
        val actual = BigUIntArithmetic.getProbablePrime(size)

        // Then
        actual.length mustBe ceil(size.toDouble() / 8)
    }
}
