package io.bitpogo.krump.bignumber

import java.math.BigInteger
import org.junit.Test
import tech.antibytes.util.test.mustBe

class BigUIntArithmeticSpec {

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun Given_two_numbers_it_adds_them() {
        // Given
        val value = UByteArray(2)
        value[0] = 1u
        value[1] = 42u

        val summand1 = BigInteger(value.asByteArray()).toByteArray()
        val summand2 = BigInteger(value.asByteArray()).toByteArray()

        // When
        val actual = BigUIntArithmetic.add(summand1, summand2)

        // Then
        BigInteger(actual).toString(10) mustBe "596"
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun Given_two_numbers_it_subtracts_them() {
        // Given
        val value = UByteArray(2)
        value[0] = 1u
        value[1] = 42u

        val minuend = BigInteger(value.asByteArray()).toByteArray()
        val subtrahend = BigInteger(value.asByteArray()).toByteArray()

        // When
        val actual = BigUIntArithmetic.subtract(minuend, subtrahend)

        // Then
        BigInteger(actual).toString(10) mustBe "0"
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun Given_two_numbers_it_multiplies_them() {
        // Given
        val value = UByteArray(2)
        value[0] = 1u
        value[1] = 42u

        val factor1 = BigInteger(value.asByteArray()).toByteArray()
        val factor2 = BigInteger(value.asByteArray()).toByteArray()

        // When
        val actual = BigUIntArithmetic.multiply(factor1, factor2)

        // Then
        BigInteger(actual).toString(10) mustBe "88804"
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun Given_two_numbers_it_diviedes_them() {
        // Given
        val valueDividend = UByteArray(3)
        valueDividend[0] = 1u
        valueDividend[1] = 90u
        valueDividend[2] = 228u

        val valueDivisor = UByteArray(2)
        valueDivisor[0] = 1u
        valueDivisor[1] = 42u

        val dividend = BigInteger(valueDividend.asByteArray()).toByteArray()
        val divisor = BigInteger(valueDivisor.asByteArray()).toByteArray()

        // When
        val actual = BigUIntArithmetic.divide(dividend, divisor)

        // Then
        BigInteger(actual).toString(10) mustBe "298"
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun Given_two_numbers_it_detetermines_the_remainder() {
        // Given
        val valueNumber = UByteArray(3)
        valueNumber[0] = 1u
        valueNumber[1] = 90u
        valueNumber[2] = 228u

        val valueModulus = UByteArray(2)
        valueModulus[0] = 1u
        valueModulus[1] = 42u

        val number = BigInteger(valueNumber.asByteArray()).toByteArray()
        val modulus = BigInteger(valueModulus.asByteArray()).toByteArray()

        // When
        val actual = BigUIntArithmetic.remainder(number, modulus)

        // Then
        BigInteger(actual).toString(10) mustBe "0"
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun Given_two_numbers_it_detetermines_the_greatest_common_divisor() {
        // Given
        val valueNumber = UByteArray(3)
        valueNumber[0] = 1u
        valueNumber[1] = 90u
        valueNumber[2] = 228u

        val valueOther = UByteArray(2)
        valueOther[0] = 1u
        valueOther[1] = 42u

        val number = BigInteger(valueNumber.asByteArray()).toByteArray()
        val other = BigInteger(valueOther.asByteArray()).toByteArray()

        // When
        val actual = BigUIntArithmetic.gcd(number, other)

        // Then
        BigInteger(actual).toString(10) mustBe "298"
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun Given_two_numbers_it_left_shifts() {
        // Given
        val valueNumber = UByteArray(3)
        valueNumber[0] = 1u
        valueNumber[1] = 90u
        valueNumber[2] = 228u

        val number = BigInteger(valueNumber.asByteArray()).toByteArray()
        val other = 298L

        // When
        val actual = BigUIntArithmetic.shiftLeft(number, other)

        // Then
        BigInteger(actual).toString(10) mustBe "45224235710601925601245762728376604553503649807777450700372954116116619236045838213977605144576"
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun Given_two_numbers_it_right_shifts() {
        // Given
        val valueNumber = UByteArray(3)
        valueNumber[0] = 1u
        valueNumber[1] = 90u
        valueNumber[2] = 228u

        val number = BigInteger(valueNumber.asByteArray()).toByteArray()
        val other = 10L

        // When
        val actual = BigUIntArithmetic.shiftRight(number, other)

        // Then
        BigInteger(actual).toString(10) mustBe "86"
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun Given_three_numbers_it_pows_the_base_with_exponent_while_calculating_the_remainder() {
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

        val base = BigInteger(valueBase.asByteArray()).toByteArray()
        val exponent = BigInteger(valueExponent.asByteArray()).toByteArray()
        val modulus = BigInteger(valueModulus.asByteArray()).toByteArray()

        // When
        val actual = BigUIntArithmetic.modPow(base, exponent, modulus)

        // Then
        BigInteger(actual).toString(10) mustBe "10"
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun Given_two_numbers_it_determines_the_multiplicative_inverse() {
        // Given
        val valueNumber = UByteArray(3)
        valueNumber[0] = 1u
        valueNumber[1] = 90u
        valueNumber[2] = 228u

        val valueModulus = UByteArray(2)
        valueModulus[0] = 1u
        valueModulus[1] = 42u

        val number = BigInteger(valueNumber.asByteArray()).toByteArray()
        val modulus = BigInteger(valueModulus.asByteArray()).toByteArray()

        // When
        val actual = BigUIntArithmetic.modInverse(number, modulus)

        // Then
        actual.size mustBe 0
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun Given_a_number_it_transforms_it_into_a_string() {
        // Given
        val valueToString = UByteArray(3)
        valueToString[0] = 1u
        valueToString[1] = 90u
        valueToString[2] = 228u

        val number = BigInteger(valueToString.asByteArray())

        // When
        val actual = BigUIntArithmetic.intoString(number.toByteArray(), 10)

        // Then
        actual mustBe number.toString(10)
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun Given_tow_numbers_it_compares_them() {
        // Given
        val valueToString = UByteArray(3)
        valueToString[0] = 1u
        valueToString[1] = 90u
        valueToString[2] = 228u

        val number = BigInteger(valueToString.asByteArray())

        // When
        val actual = BigUIntArithmetic.compare(number.toByteArray(), number.toByteArray())

        // Then
        actual mustBe 0
    }
}
