/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.krump.bignumber

import kotlin.math.abs
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kmock.MockShared
import tech.antibytes.kmock.verification.assertProxy
import tech.antibytes.util.test.mustBe

@MockShared(
    "concurrent",
    BigUIntegerContract.BigUIntArithmetic::class,
)
class BigUIntegerSpec {
    private val fixture = kotlinFixture()
    private val rechenwerk: BigUIntArithmeticMock = kmock()

    @BeforeTest
    fun setUp() {
        rechenwerk._clearMock()
    }

    @Test
    fun Given_toString_is_called_it_returns_the_value_of_the_given_Number() {
        // Given
        val number: Long = abs(fixture.fixture<Long>())

        rechenwerk._intoString returns number.toString()

        // When
        val actual = BigUIntegerFactory(rechenwerk).from(number.toString())

        // Then
        actual.toString() mustBe number.toString()
    }

    @Test
    fun Given_equals_is_called_it_returns_false_if_the_given_other_has_not_the_same_value() {
        // Given
        val value = fixture.fixture<UInt>()
        val other: Any? = fixture.fixture()
        val number = BigUIntegerFactory(rechenwerk).from(value)

        rechenwerk._intoString returns value.toString()

        // When
        val actual = number == other

        // Then
        actual mustBe false
    }

    @Test
    fun Given_equals_is_called_it_returns_true_if_the_given_other_has_the_same_value() {
        // Given
        val value = fixture.fixture<UInt>()
        val number = BigUIntegerFactory(rechenwerk).from(value)

        rechenwerk._intoString returns value.toString()

        // When
        val actual = number.equals(value)

        // Then
        actual mustBe true
    }

    @Test
    fun Given_toUByteArray_is_called_it_returns_the_wrapped_ByteArray_as_a_UByteArray() {
        // Given
        val value: UByteArray = fixture.fixture()

        rechenwerk._intoString returns value.toString()

        // When
        val actual = BigUInteger(rechenwerk, value).toUByteArray()

        // Then
        actual mustBe value
    }

    @Test
    fun Given_toByteArray_is_called_it_returns_the_wrapped_ByteArray() {
        // Given
        val value: UByteArray = fixture.fixture()

        // When
        val actual = BigUInteger(rechenwerk, value).toByteArray()

        // Then
        actual.contentEquals(value.asByteArray()) mustBe true
    }

    @Test
    fun Given_compareTo_is_called_it_returns_a_positive_Integer_if_the_number1_is_larger_than_number2() {
        // Given
        val number1 = BigUIntegerFactory(rechenwerk).from("23")
        val number2 = BigUIntegerFactory(rechenwerk).from("42")

        rechenwerk._compare returns 1

        // When
        val actual = number1 > number2

        // Then
        actual mustBe true
    }

    @Test
    fun Given_compareTo_is_called_it_returns_a_negative_Integer_if_the_number2_is_larger_than_number2() {
        // Given
        val number1 = BigUIntegerFactory(rechenwerk).from("23")
        val number2 = BigUIntegerFactory(rechenwerk).from("42")

        rechenwerk._compare returns -1

        // When
        val actual = number1 < number2

        // Then
        actual mustBe true
    }

    @Test
    fun Given_compareTo_is_called_it_returns_zero_if_the_number1_and_number2_have_the_same_value() {
        // Given
        val number1 = BigUIntegerFactory(rechenwerk).from("23")
        val number2 = BigUIntegerFactory(rechenwerk).from("42")

        rechenwerk._compare returns 0

        // When
        val actual = number1 <= number2

        // Then
        actual mustBe true
    }

    @Test
    fun Given_plus_is_called_it_delegates_the_call_to_the_Rechenwerk_and_returns_its_result() {
        // Given
        val summand1: UByteArray = fixture.fixture()
        val summand2: UByteArray = fixture.fixture()
        val result: ByteArray = fixture.fixture()

        rechenwerk._add.returnValue = result

        // When
        val actual = BigUInteger(rechenwerk, summand1) + BigUInteger(rechenwerk, summand2)

        // Then
        actual.toByteArray() mustBe result

        assertProxy {
            rechenwerk._add.hasBeenStrictlyCalledWith(
                summand1.asByteArray(),
                summand2.asByteArray(),
            )
        }
    }

    @Test
    fun Given_minus_is_called_it_fails_if_the_minuend_is_smaller_than_the_subtrahend() {
        // Given
        val minuend = arrayOf(23.toUByte()).toUByteArray()
        val subtrahend = arrayOf(42.toUByte()).toUByteArray()

        rechenwerk._compare returns -1

        // When
        val error = assertFailsWith<IllegalArgumentException> {
            BigUInteger(rechenwerk, minuend) - BigUInteger(rechenwerk, subtrahend)
        }

        // Then
        error.message mustBe "The minuend must be greater than the subtrahend!"
    }

    @Test
    fun Given_minus_is_called_it_delegates_the_call_to_the_Rechenwerk_and_returns_its_result() {
        // Given
        val minuend = arrayOf(42.toUByte()).toUByteArray()
        val subtrahend = arrayOf(23.toUByte()).toUByteArray()
        val result: ByteArray = fixture.fixture()

        rechenwerk._subtract.returnValue = result
        rechenwerk._compare returns 1

        // When
        val actual = BigUInteger(rechenwerk, minuend) - BigUInteger(rechenwerk, subtrahend)

        // Then
        actual.toByteArray() mustBe result

        assertProxy {
            rechenwerk._subtract.hasBeenStrictlyCalledWith(
                minuend.asByteArray(),
                subtrahend.asByteArray(),
            )
        }
    }

    @Test
    fun Given_times_is_called_it_delegates_the_call_to_the_Rechenwerk_and_returns_its_result() {
        // Given
        val factor1: UByteArray = fixture.fixture()
        val factor2: UByteArray = fixture.fixture()
        val result: ByteArray = fixture.fixture()

        rechenwerk._multiply.returnValue = result

        // When
        val actual = BigUInteger(rechenwerk, factor1) * BigUInteger(rechenwerk, factor2)

        // Then
        actual.toByteArray() mustBe result

        assertProxy {
            rechenwerk._multiply.hasBeenStrictlyCalledWith(
                factor1.asByteArray(),
                factor2.asByteArray(),
            )
        }
    }

    @Test
    fun Given_div_is_called_it_delegates_the_call_to_the_Rechenwerk_and_returns_its_result() {
        // Given
        val dividend: UByteArray = fixture.fixture()
        val divisor: UByteArray = fixture.fixture()
        val result: ByteArray = fixture.fixture()

        rechenwerk._divide.returnValue = result

        // When
        val actual = BigUInteger(rechenwerk, dividend) / BigUInteger(rechenwerk, divisor)

        // Then
        actual.toByteArray() mustBe result

        assertProxy {
            rechenwerk._divide.hasBeenStrictlyCalledWith(
                dividend.asByteArray(),
                divisor.asByteArray(),
            )
        }
    }

    @Test
    fun Given_rem_is_called_it_delegates_the_call_to_the_Rechenwerk_and_returns_its_result() {
        // Given
        val number: UByteArray = fixture.fixture()
        val modulus: UByteArray = fixture.fixture()
        val result: ByteArray = fixture.fixture()

        rechenwerk._remainder.returnValue = result

        // When
        val actual = BigUInteger(rechenwerk, number) % BigUInteger(rechenwerk, modulus)

        // Then
        actual.toByteArray() mustBe result

        assertProxy {
            rechenwerk._remainder.hasBeenStrictlyCalledWith(
                number.asByteArray(),
                modulus.asByteArray(),
            )
        }
    }

    @Test
    fun Given_gcd_is_called_it_delegates_the_call_to_the_Rechenwerk_and_returns_its_result() {
        // Given
        val number: UByteArray = fixture.fixture()
        val other: UByteArray = fixture.fixture()
        val result: ByteArray = fixture.fixture()

        rechenwerk._gcd.returnValue = result

        // When
        val actual = BigUInteger(rechenwerk, number).gcd(BigUInteger(rechenwerk, other))

        // Then
        actual.toByteArray() mustBe result

        assertProxy {
            rechenwerk._gcd.hasBeenStrictlyCalledWith(
                number.asByteArray(),
                other.asByteArray(),
            )
        }
    }

    @Test
    fun Given_shl_is_called_it_delegates_the_call_to_the_Rechenwerk_and_returns_its_result() {
        // Given
        val number: UByteArray = fixture.fixture()
        val shifts = UInt.MAX_VALUE
        val result: ByteArray = fixture.fixture()

        rechenwerk._shiftLeft.returnValue = result

        // When
        val actual = BigUInteger(rechenwerk, number) shl shifts

        // Then
        actual.toByteArray() mustBe result

        assertProxy {
            rechenwerk._shiftLeft.hasBeenStrictlyCalledWith(
                number.asByteArray(),
                shifts.toLong(),
            )
        }
    }

    @Test
    fun Given_shr_is_called_it_delegates_the_call_to_the_Rechenwerk_and_returns_its_result() {
        // Given
        val number: UByteArray = fixture.fixture()
        val shifts = UInt.MAX_VALUE
        val result: ByteArray = fixture.fixture()

        rechenwerk._shiftRight.returnValue = result

        // When
        val actual = BigUInteger(rechenwerk, number) shr shifts

        // Then
        actual.toByteArray() mustBe result

        assertProxy {
            rechenwerk._shiftRight.hasBeenStrictlyCalledWith(
                number.asByteArray(),
                shifts.toLong(),
            )
        }
    }

    @Test
    fun Given_modInverse_is_called_it_delegates_the_call_to_the_Rechenwerk_and_fails_if_resulting_ByteArray_isEmpty() {
        // Given
        val number: UByteArray = fixture.fixture()
        val modulus: UByteArray = fixture.fixture()
        val result = ByteArray(0)

        rechenwerk._modInverse.returnValue = result

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            BigUInteger(rechenwerk, number).modInverse(BigUInteger(rechenwerk, modulus))
        }

        error.message mustBe "The multiplicative inverse does not exists!"
    }

    @Test
    fun Given_modInverse_is_called_it_delegates_the_call_to_the_Rechenwerk_and_returns_its_result() {
        // Given
        val number: UByteArray = fixture.fixture()
        val modulus: UByteArray = fixture.fixture()
        val result: ByteArray = fixture.fixture()

        rechenwerk._modInverse.returnValue = result

        // When
        val actual = BigUInteger(rechenwerk, number).modInverse(BigUInteger(rechenwerk, modulus))

        // Then
        actual.toByteArray() mustBe result

        assertProxy {
            rechenwerk._modInverse.hasBeenStrictlyCalledWith(
                number.asByteArray(),
                modulus.asByteArray(),
            )
        }
    }

    @Test
    fun Given_modPow_is_called_it_delegates_the_call_to_the_Rechenwerk_and_returns_its_result() {
        // Given
        val base: UByteArray = fixture.fixture()
        val exponent: UByteArray = fixture.fixture()
        val modulus: UByteArray = fixture.fixture()
        val result: ByteArray = fixture.fixture()

        rechenwerk._modPow.returnValue = result

        // When
        val actual = BigUInteger(rechenwerk, base).modPow(
            exponent = BigUInteger(rechenwerk, exponent),
            modulus = BigUInteger(rechenwerk, modulus),
        )

        // Then
        actual.toByteArray() mustBe result

        assertProxy {
            rechenwerk._modPow.hasBeenStrictlyCalledWith(
                base.asByteArray(),
                exponent.asByteArray(),
                modulus.asByteArray(),
            )
        }
    }
}
