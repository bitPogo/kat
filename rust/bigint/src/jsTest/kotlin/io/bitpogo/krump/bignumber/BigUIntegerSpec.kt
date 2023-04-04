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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kmock.KMockContract.ArgumentConstraint
import tech.antibytes.kmock.Mock
import tech.antibytes.kmock.verification.assertProxy
import tech.antibytes.util.test.mustBe

@Suppress("ClassName")
private class eqUint8Array(private val array: Uint8Array) : ArgumentConstraint {
    private fun compareElements(actual: Uint8Array): Boolean {
        var equals = true

        (0..actual.length).forEach { idx ->
            equals = equals and (actual[idx] == array[idx])
        }

        return equals
    }

    private fun deepMatch(actual: Uint8Array): Boolean {
        return if (actual.length != array.length) {
            false
        } else {
            compareElements(actual)
        }
    }

    override fun matches(actual: Any?): Boolean {
        return if (actual is Uint8Array) {
            deepMatch(actual)
        } else {
            false
        }
    }
}

@Mock(
    BigUIntegerContract.BigUIntArithmetic::class,
)
@OptIn(ExperimentalCoroutinesApi::class)
class BigUIntegerSpec {
    private val fixture = kotlinFixture()
    private val rechenwerk: BigUIntArithmeticMock = kmock()

    private fun ByteArray.asUint8Array(): Uint8Array = Uint8Array(this.toTypedArray())

    @BeforeTest
    fun setUp() {
        rechenwerk._clearMock()
    }

    @Test
    fun Given_asString_is_called_it_returns_the_value_of_the_given_Number() = runTest {
        // Given
        val number: Long = abs(fixture.fixture<Long>())

        rechenwerk._intoString returns number.toString()

        // When
        val actual = BigUIntegerFactory(rechenwerk).from(number.toString())

        // Then
        actual.asString() mustBe number.toString()
    }

    @Test
    fun Given_equals_is_called_it_returns_false_if_the_given_other_is_not_the_same() {
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
    fun Given_equals_is_called_it_returns_true_if_the_given_other_is_the_same() {
        // Given
        val value = fixture.fixture<UInt>()
        val number = BigUIntegerFactory(rechenwerk).from(value)

        rechenwerk._intoString returns value.toString()

        // When
        val actual = number.equals(number)

        // Then
        actual mustBe true
    }

    @Test
    fun Given_equalsTo_is_called_it_returns_false_if_the_given_other_has_not_the_same_value() = runTest {
        // Given
        val value = fixture.fixture<UInt>()
        val other: Any? = fixture.fixture()
        val number = BigUIntegerFactory(rechenwerk).from(value)

        rechenwerk._intoString returns value.toString()

        // When
        val actual = number.equalsTo(other)

        // Then
        actual mustBe false
    }

    @Test
    fun Given_equalsTo_is_called_it_returns_true_if_the_given_other_has_the_same_value() = runTest {
        // Given
        val value = fixture.fixture<UInt>()
        val number = BigUIntegerFactory(rechenwerk).from(value)

        rechenwerk._intoString returns value.toString()

        // When
        val actual = number.equalsTo(value)

        // Then
        actual mustBe true
    }

    @Test
    fun Given_equalsTo_is_called_it_returns_true_if_the_given_other_is_BigUInt_has_the_same_value() = runTest {
        // Given
        val value = fixture.fixture<UInt>()
        val number = BigUIntegerFactory(rechenwerk).from(value)

        rechenwerk._intoString returns value.toString()

        // When
        val actual = number.equalsTo(number)

        // Then
        actual mustBe true
    }

    @Test
    fun Given_toByteArray_is_called_it_returns_the_wrapped_ByteArray_as_a_UByteArray() {
        // Given
        val value: ByteArray = fixture.fixture()

        // When
        val actual = BigUInteger(bytes = value, rechenwerk = rechenwerk).toUByteArray()

        // Then
        actual.contentEquals(value.asUByteArray()) mustBe true
    }

    @Test
    fun Given_toByteArray_is_called_it_returns_the_wrapped_ByteArray() {
        // Given
        val value: ByteArray = fixture.fixture()

        // When
        val actual = BigUInteger(bytes = value, rechenwerk = rechenwerk).toByteArray()

        // Then
        actual.contentEquals(value) mustBe true
    }

    @Test
    fun Given_compareTo_is_called_it_returns_a_positive_Integer_if_the_number1_is_larger_than_number2() = runTest {
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
    fun Given_compareTo_is_called_it_returns_a_negative_Integer_if_the_number2_is_larger_than_number2() = runTest {
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
    fun Given_compareTo_is_called_it_returns_zero_if_the_number1_and_number2_have_the_same_value() = runTest {
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
    fun Given_plus_is_called_it_delegates_the_call_to_the_Rechenwerk_and_returns_its_result() = runTest {
        // Given
        val summand1: ByteArray = fixture.fixture()
        val summand2: ByteArray = fixture.fixture()
        val result: ByteArray = fixture.fixture()

        rechenwerk._add.returnValue = result.asUint8Array()

        // When
        val actual = BigUInteger(bytes = summand1, rechenwerk = rechenwerk) + BigUInteger(bytes = summand2, rechenwerk = rechenwerk)

        // Then
        actual.toByteArray().contentEquals(result) mustBe true

        assertProxy {
            rechenwerk._add.hasBeenStrictlyCalledWith(
                eqUint8Array(summand1.asUint8Array()),
                eqUint8Array(summand2.asUint8Array()),
            )
        }
    }

    @Test
    fun Given_minus_is_called_it_fails_if_the_minuend_is_smaller_than_the_subtrahend() = runTest {
        // Given
        val minuend = arrayOf(23.toByte()).toByteArray()
        val subtrahend = arrayOf(42.toByte()).toByteArray()

        rechenwerk._compare returns -1

        // When
        val error = assertFailsWith<IllegalArgumentException> {
            BigUInteger(bytes = minuend, rechenwerk = rechenwerk) - BigUInteger(bytes = subtrahend, rechenwerk = rechenwerk)
        }

        // Then
        error.message mustBe "The minuend must be greater than the subtrahend!"
    }

    @Test
    fun Given_minus_is_called_it_delegates_the_call_to_the_Rechenwerk_and_returns_its_result() = runTest {
        // Given
        val minuend = arrayOf(42.toByte()).toByteArray()
        val subtrahend = arrayOf(23.toByte()).toByteArray()
        val result: ByteArray = fixture.fixture()

        rechenwerk._subtract.returnValue = result.asUint8Array()
        rechenwerk._compare returns 1

        // When
        val actual = BigUInteger(bytes = minuend, rechenwerk = rechenwerk) - BigUInteger(bytes = subtrahend, rechenwerk = rechenwerk)

        // Then
        actual.toByteArray().contentEquals(result) mustBe true

        assertProxy {
            rechenwerk._subtract.hasBeenStrictlyCalledWith(
                eqUint8Array(minuend.asUint8Array()),
                eqUint8Array(subtrahend.asUint8Array()),
            )
        }
    }

    @Test
    fun Given_times_is_called_it_delegates_the_call_to_the_Rechenwerk_and_returns_its_result() = runTest {
        // Given
        val factor1: ByteArray = fixture.fixture()
        val factor2: ByteArray = fixture.fixture()
        val result: ByteArray = fixture.fixture()

        rechenwerk._multiply.returnValue = result.asUint8Array()

        // When
        val actual = BigUInteger(bytes = factor1, rechenwerk = rechenwerk) * BigUInteger(bytes = factor2, rechenwerk = rechenwerk)

        // Then
        actual.toByteArray().contentEquals(result) mustBe true

        assertProxy {
            rechenwerk._multiply.hasBeenStrictlyCalledWith(
                eqUint8Array(factor1.asUint8Array()),
                eqUint8Array(factor2.asUint8Array()),
            )
        }
    }

    @Test
    fun Given_div_is_called_it_delegates_the_call_to_the_Rechenwerk_and_returns_its_result() = runTest {
        // Given
        val dividend: ByteArray = fixture.fixture()
        val divisor: ByteArray = fixture.fixture()
        val result: ByteArray = fixture.fixture()

        rechenwerk._divide.returnValue = result.asUint8Array()

        // When
        val actual = BigUInteger(bytes = dividend, rechenwerk = rechenwerk) / BigUInteger(bytes = divisor, rechenwerk = rechenwerk)

        // Then
        actual.toByteArray().contentEquals(result) mustBe true

        assertProxy {
            rechenwerk._divide.hasBeenStrictlyCalledWith(
                eqUint8Array(dividend.asUint8Array()),
                eqUint8Array(divisor.asUint8Array()),
            )
        }
    }

    @Test
    fun Given_rem_is_called_it_delegates_the_call_to_the_Rechenwerk_and_returns_its_result() = runTest {
        // Given
        val number: ByteArray = fixture.fixture()
        val modulus: ByteArray = fixture.fixture()
        val result: ByteArray = fixture.fixture()

        rechenwerk._remainder.returnValue = result.asUint8Array()

        // When
        val actual = BigUInteger(bytes = number, rechenwerk = rechenwerk) % BigUInteger(bytes = modulus, rechenwerk = rechenwerk)

        // Then
        actual.toByteArray().contentEquals(result) mustBe true

        assertProxy {
            rechenwerk._remainder.hasBeenStrictlyCalledWith(
                eqUint8Array(number.asUint8Array()),
                eqUint8Array(modulus.asUint8Array()),
            )
        }
    }

    @Test
    fun Given_gcd_is_called_it_delegates_the_call_to_the_Rechenwerk_and_returns_its_result() = runTest {
        // Given
        val number: ByteArray = fixture.fixture()
        val other: ByteArray = fixture.fixture()
        val result: ByteArray = fixture.fixture()

        rechenwerk._gcd.returnValue = result.asUint8Array()

        // When
        val actual = BigUInteger(bytes = number, rechenwerk = rechenwerk).gcd(BigUInteger(bytes = other, rechenwerk = rechenwerk))

        // Then
        actual.toByteArray().contentEquals(result) mustBe true

        assertProxy {
            rechenwerk._gcd.hasBeenStrictlyCalledWith(
                eqUint8Array(number.asUint8Array()),
                eqUint8Array(other.asUint8Array()),
            )
        }
    }

    @Test
    fun Given_shl_is_called_it_delegates_the_call_to_the_Rechenwerk_and_returns_its_result() = runTest {
        // Given
        val number: ByteArray = fixture.fixture()
        val shifts = UInt.MAX_VALUE
        val result: ByteArray = fixture.fixture()

        rechenwerk._shiftLeft.returnValue = result.asUint8Array()

        // When
        val actual = BigUInteger(bytes = number, rechenwerk = rechenwerk) shl shifts

        // Then
        actual.toByteArray().contentEquals(result) mustBe true

        assertProxy {
            rechenwerk._shiftLeft.hasBeenStrictlyCalledWith(
                eqUint8Array(number.asUint8Array()),
                shifts.toLong(),
            )
        }
    }

    @Test
    fun Given_shr_is_called_it_delegates_the_call_to_the_Rechenwerk_and_returns_its_result() = runTest {
        // Given
        val number: ByteArray = fixture.fixture()
        val shifts = UInt.MAX_VALUE
        val result: ByteArray = fixture.fixture()

        rechenwerk._shiftRight.returnValue = result.asUint8Array()

        // When
        val actual = BigUInteger(bytes = number, rechenwerk = rechenwerk) shr shifts

        // Then
        actual.toByteArray().contentEquals(result) mustBe true

        assertProxy {
            rechenwerk._shiftRight.hasBeenStrictlyCalledWith(
                eqUint8Array(number.asUint8Array()),
                shifts.toLong(),
            )
        }
    }

    @Test
    fun Given_modInverse_is_called_it_delegates_the_call_to_the_Rechenwerk_and_fails_if_resulting_ByteArray_isEmpty() = runTest {
        // Given
        val number: ByteArray = fixture.fixture()
        val modulus: ByteArray = fixture.fixture()
        val result = ByteArray(0)

        rechenwerk._modInverse.returnValue = result.asUint8Array()

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            BigUInteger(bytes = number, rechenwerk = rechenwerk).modInverse(BigUInteger(bytes = modulus, rechenwerk = rechenwerk))
        }

        error.message mustBe "The multiplicative inverse does not exists!"
    }

    @Test
    fun Given_modInverse_is_called_it_delegates_the_call_to_the_Rechenwerk_and_returns_its_result() = runTest {
        // Given
        val number: ByteArray = fixture.fixture()
        val modulus: ByteArray = fixture.fixture()
        val result: ByteArray = fixture.fixture()

        rechenwerk._modInverse.returnValue = result.asUint8Array()

        // When
        val actual = BigUInteger(bytes = number, rechenwerk = rechenwerk).modInverse(BigUInteger(bytes = modulus, rechenwerk = rechenwerk))

        // Then
        actual.toByteArray().contentEquals(result) mustBe true

        assertProxy {
            rechenwerk._modInverse.hasBeenStrictlyCalledWith(
                eqUint8Array(number.asUint8Array()),
                eqUint8Array(modulus.asUint8Array()),
            )
        }
    }

    @Test
    fun Given_modPow_is_called_it_delegates_the_call_to_the_Rechenwerk_and_returns_its_result() = runTest {
        // Given
        val base: ByteArray = fixture.fixture()
        val exponent: ByteArray = fixture.fixture()
        val modulus: ByteArray = fixture.fixture()
        val result: ByteArray = fixture.fixture()

        rechenwerk._modPow.returnValue = result.asUint8Array()

        // When
        val actual = BigUInteger(bytes = base, rechenwerk = rechenwerk).modPow(
            exponent = BigUInteger(bytes = exponent, rechenwerk = rechenwerk),
            modulus = BigUInteger(bytes = modulus, rechenwerk = rechenwerk),
        )

        // Then
        actual.toByteArray().contentEquals(result) mustBe true

        assertProxy {
            rechenwerk._modPow.hasBeenStrictlyCalledWith(
                eqUint8Array(base.asUint8Array()),
                eqUint8Array(exponent.asUint8Array()),
                eqUint8Array(modulus.asUint8Array()),
            )
        }
    }
}
