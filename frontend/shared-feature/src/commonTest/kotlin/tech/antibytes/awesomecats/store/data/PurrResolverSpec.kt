/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store.data

import io.bitpogo.krump.bignumber.BigUIntegerFactoryMock
import io.bitpogo.krump.bignumber.BigUIntegerMock
import kotlinx.coroutines.test.runTest
import tech.antibytes.awesomecats.viewmodel.kmock
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.kmock.verification.assertProxy
import tech.antibytes.util.test.annotations.IgnoreNative
import tech.antibytes.util.test.annotations.NativeOnly
import tech.antibytes.util.test.mustBe
import kotlin.js.JsName
import kotlin.math.absoluteValue
import kotlin.random.Random
import kotlin.test.Test

expect interface PurrLevel

@OptIn(KMockExperimental::class)
@KMock(
    PurrMultiplier::class,
    PurrLevel::class
)
class PurrResolverSpec {
    private val fixture = kotlinFixture()

    @Test
    @JsName("Js0")
    fun `Given resolves is called it retrieves 2 Ints and multiplies them`() = runTest {
        // Given
        val number1 = fixture.fixture<Int>(0..100)
        val number2 = fixture.fixture<Int>(0..100)
        val resultValue = fixture.fixture<Int>(0..100)

        val random = TestRandom(listOf(number1, number2))
        val multiplier: BigUIntegerFactoryMock = kmock()
        val result: BigUIntegerMock = kmock()

        multiplier._fromWithString returns result
        result._times returns result
        result._toString returns resultValue.toString()

        // When
        val actual = PurrResolver(random, multiplier as PurrMultiplier).resolve()

        // Then
        actual mustBe resultValue.toString()

        assertProxy {
            multiplier._fromWithString.hasBeenCalledWith(number1.toString())
            multiplier._fromWithString.hasBeenCalledWith(number2.toString())
            result._times.hasBeenCalledWith(result)
            result._toString.hasBeenCalled()
        }
    }

    @Test
    @JsName("Js1")
    fun `Given resolves is called it retrieves 2 Ints and multiplies them while using the absolute value`() = runTest {
        // Given
        val number1 = fixture.fixture<Int>(0..100) * -1
        val number2 = fixture.fixture<Int>(0..100) * -1
        val resultValue = fixture.fixture<Int>(0..100)

        val random = TestRandom(listOf(number1, number2))
        val multiplier: BigUIntegerFactoryMock = kmock()
        val result: BigUIntegerMock = kmock()

        multiplier._fromWithString returns result
        result._times returns result
        result._toString returns resultValue.toString()

        // When
        val actual = PurrResolver(random, multiplier as PurrMultiplier).resolve()

        // Then
        actual mustBe resultValue.toString()

        assertProxy {
            multiplier._fromWithString.hasBeenCalledWith(number1.absoluteValue.toString())
            multiplier._fromWithString.hasBeenCalledWith(number2.absoluteValue.toString())
            result._times.hasBeenCalledWith(result)
            result._toString.hasBeenCalled()
        }
    }

    private class TestRandom(nextInts: List<Int>) : Random() {
        private val nextInts = nextInts.toMutableList()
        override fun nextBits(bitCount: Int): Int = TODO("Not yet implemented")
        override fun nextInt(): Int = nextInts.removeFirst()
    }

}