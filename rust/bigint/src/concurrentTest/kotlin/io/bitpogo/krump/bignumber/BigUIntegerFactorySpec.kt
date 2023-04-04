/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.krump.bignumber

import kotlin.js.JsName
import kotlin.math.abs
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kmock.MockShared
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

@MockShared(
    "concurrent",
    BigUIntegerContract.BigUIntArithmetic::class,
)
class BigUIntegerFactorySpec {
    private val rechenwerk: BigUIntArithmeticMock = kmock()
    private val fixture = kotlinFixture()

    @BeforeTest
    fun setUp() {
        rechenwerk._clearMock()
    }

    @Test
    @JsName("fn0")
    fun It_fulfils_BigUIntegerFactory() {
        BigUIntegerFactory(rechenwerk) fulfils BigUIntegerContract.BigUIntegerFactory::class
    }

    @Test
    @JsName("fn1")
    fun Given_from_is_called_with_a_String_it_fails_for_signed_numbers() {
        // Given
        val number: Long = -1 * abs(fixture.fixture<Long>())

        // Then
        val error = assertFailsWith<IllegalArgumentException> {
            // When
            BigUIntegerFactory(rechenwerk).from(number.toString())
        }

        error.message mustBe "Signed Numbers are forbidden!"
    }

    @Test
    @JsName("fn1a")
    fun Given_from_is_called_with_a_String_it_returns_a_BigUInteger() {
        // Given
        val number = abs(fixture.fixture<Long>())

        // When
        val actual: Any = BigUIntegerFactory(rechenwerk).from(number.toString())

        // Then
        actual fulfils BigUIntegerContract.BigUInteger::class
    }

    @Test
    @JsName("fn2")
    fun Given_from_is_called_with_an_Integer_it_returns_a_BigUInteger() {
        // Given
        val number: UInt = fixture.fixture()

        // When
        val actual: Any = BigUIntegerFactory(rechenwerk).from(number)

        // Then
        actual fulfils BigUIntegerContract.BigUInteger::class
    }

    @Test
    @JsName("fn3")
    fun Given_from_is_called_with_a_ByteArray_it_returns_a_BigUInteger() {
        // Given
        val number: UByteArray = fixture.fixture()

        // When
        val actual: Any = BigUIntegerFactory(rechenwerk).from(number)

        // Then
        actual fulfils BigUIntegerContract.BigUInteger::class
    }

    /*@Test
    @JsName("fn5")
    fun Given_getProbablePrime_is_called_with_a_Int_which_is_smaller_then_2_it_fails() {
        // Then
        val error = assertFailsWith<IllegalArgumentException> {
            // When
            BigUIntegerFactory(rechenwerk).getProbablePrime(1)
        }

        // Then
        error.message mustBe "Prime size must be at least 2-bit."
    }*/

    @Test
    fun Given_from_is_called_with_a_String_it_does_not_change_the_bytes() {
        // When
        val factory = BigUIntegerFactory(rechenwerk)
        val actual = factory.from("298").toUByteArray()

        // Then
        actual.contentEquals(arrayOf(1.toUByte(), 42.toUByte()).toUByteArray()) mustBe true
    }
}
