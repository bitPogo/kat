package io.bitpogo.krump.bignumber

import tech.antibytes.util.test.mustBe
import kotlin.test.Test

class BigUIntArithmeticSpec {
    @Test
    fun Given_a_number_it_transforms_it_into_a_string() {
        // Given
        val toString = UByteArray(3)
        toString[0] = 1u
        toString[1] = 90u
        toString[2] = 228u

        // When
        val actual = BigUIntArithmetic.intoString(toString.toByteArray(), 10)

        // Then
        actual mustBe "5820628925"
    }

    @Test
    fun Given_two_numbers_it_adds_them() {
        // Given
        val summand = UByteArray(2)
        summand[0] = 1u
        summand[1] = 42u

        // When
        val actual = BigUIntArithmetic.add(
            summand.toByteArray(),
            summand.toByteArray()
        )

        // Then
        BigUIntArithmetic.intoString(actual, 10) mustBe "596"
    }
    
    @Test
    fun Given_two_numbers_it_subtracts_them() {
        // Given
        val number = UByteArray(2)
        number[0] = 1u
        number[1] = 42u

        // When
        val actual = BigUIntArithmetic.subtract(
            number.toByteArray(),
            number.toByteArray()
        )

        // Then
        BigUIntArithmetic.intoString(actual, 10) mustBe "0"
    }
    
    @Test
    fun Given_two_numbers_it_multiplies_them() {
        // Given
        val factor = UByteArray(2)
        factor[0] = 1u
        factor[1] = 42u

        // When
        val actual = BigUIntArithmetic.multiply(
            factor.toByteArray(),
            factor.toByteArray()
        )

        // Then
        BigUIntArithmetic.intoString(actual, 10) mustBe "88804"
    }

    @Test
    fun Given_two_numbers_it_diviedes_them() {
        // Given
        val dividend = UByteArray(3)
        dividend[0] = 1u
        dividend[1] = 90u
        dividend[2] = 228u
        val divisor = UByteArray(2)
        divisor[0] = 1u
        divisor[1] = 42u

        // When
        val actual = BigUIntArithmetic.divide(
            dividend.toByteArray(),
            divisor.toByteArray(),
        )

        // Then
        BigUIntArithmetic.intoString(actual, 10) mustBe "298"
    }
    
    @Test
    fun Given_two_numbers_it_detetermines_the_remainder() {
        // Given
        val number = UByteArray(3)
        number[0] = 1u
        number[1] = 90u
        number[2] = 228u

        val modulus = UByteArray(2)
        modulus[0] = 1u
        modulus[1] = 42u

        // When
        val actual = BigUIntArithmetic.remainder(
            number.toByteArray(),
            modulus.toByteArray(),
        )

        // Then
        BigUIntArithmetic.intoString(actual, 10) mustBe "0"
    }

    @Test
    fun Given_two_numbers_it_detetermines_the_greatest_common_divisor() {
        // Given
        val number = UByteArray(3)
        number[0] = 1u
        number[1] = 90u
        number[2] = 228u

        val other = UByteArray(2)
        other[0] = 1u
        other[1] = 42u

        // When
        val actual = BigUIntArithmetic.gcd(
            number.toByteArray(),
            other.toByteArray(),
        )

        // Then
        BigUIntArithmetic.intoString(actual, 10) mustBe "298"
    }

    @Test
    fun Given_two_numbers_it_left_shifts() {
        // Given
        val number = UByteArray(3)
        number[0] = 1u
        number[1] = 90u
        number[2] = 228u
        val other = 298L

        // When
        val actual = BigUIntArithmetic.shiftLeft(number.toByteArray(), other)

        // Then
        BigUIntArithmetic.intoString(actual, 10) mustBe "45224235710601925601245762728376604553503649807777450700372954116116619236045838213977605144576"
    }

    @Test
    fun Given_two_numbers_it_right_shifts() {
        // Given
        val number = UByteArray(3)
        number[0] = 1u
        number[1] = 90u
        number[2] = 228u
        val other = 10L

        // When
        val actual = BigUIntArithmetic.shiftRight(number.toByteArray(), other)

        // Then
        BigUIntArithmetic.intoString(actual, 10) mustBe "86"
    }

    @Test
    fun Given_three_numbers_it_pows_the_base_with_exponent_while_calculating_the_remainder() {
        // Given
        val base = UByteArray(3)
        base[0] = 1u
        base[1] = 90u
        base[2] = 228u

        val exponent = UByteArray(2)
        exponent[0] = 1u
        exponent[1] = 42u

        val modulus = UByteArray(2)
        modulus[0] = 1u
        modulus[1] = 23u

        // When
        val actual = BigUIntArithmetic.modPow(
            base.toByteArray(),
            exponent.toByteArray(),
            modulus.toByteArray()
        )

        // Then
        BigUIntArithmetic.intoString(actual, 10) mustBe "10"
    }

    @Test
    fun Given_two_numbers_it_determines_the_multiplicative_inverse() {
        // Given
        val number = UByteArray(3)
        number[0] = 1u
        number[1] = 90u
        number[2] = 228u

        val modulus = UByteArray(2)
        modulus[0] = 1u
        modulus[1] = 42u

        // When
        val actual = BigUIntArithmetic.modInverse(
            number.toByteArray(),
            modulus.toByteArray(),
        )

        // Then
        actual.size mustBe 0
    }

    @Test
    fun Given_tow_numbers_it_compares_them() {
        // Given
        val number = UByteArray(3)
        number[0] = 1u
        number[1] = 90u
        number[2] = 228u

        // When
        val actual = BigUIntArithmetic.compare(
            number.toByteArray(),
            number.toByteArray(),
        )

        // Then
        actual mustBe 0
    }
}
