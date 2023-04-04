/* ktlint-disable filename */
/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */
package io.bitpogo.krump.bignumber.externals

external class BigInt {
    fun toString(radix: Int = definedExternally): String
}
external fun BigInt(value: dynamic): BigInt
