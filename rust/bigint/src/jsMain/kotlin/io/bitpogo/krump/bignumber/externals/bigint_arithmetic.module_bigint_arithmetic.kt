/* ktlint-disable filename */
/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:JsModule("bigint_arithmetic")
@file:JsNonModule
@file:Suppress(
    "INTERFACE_WITH_SUPERCLASS",
    "OVERRIDING_FINAL_MEMBER",
    "RETURN_TYPE_MISMATCH_ON_OVERRIDE",
    "CONFLICTING_OVERLOADS",
    "INLINE_CLASS_IN_EXTERNAL_DECLARATION_WARNING",
)

package io.bitpogo.krump.bignumber.externals

import kotlin.js.Promise
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.ArrayBufferView
import org.khronos.webgl.Uint8Array
import org.w3c.dom.url.URL
import org.w3c.fetch.Request
import org.w3c.fetch.Response

external fun add(summand1: Uint8Array, summand2: Uint8Array): Uint8Array

external fun subtract(minuend: Uint8Array, subtrahend: Uint8Array): Uint8Array

external fun multiply(factor1: Uint8Array, factor2: Uint8Array): Uint8Array

external fun divide(dividend: Uint8Array, divisor: Uint8Array): Uint8Array

external fun remainder(number: Uint8Array, modulus: Uint8Array): Uint8Array

external fun gcd(number: Uint8Array, modulus: Uint8Array): Uint8Array

external fun shiftLeft(number: Uint8Array, shifts: String): Uint8Array

external fun shiftRight(number: Uint8Array, shifts: String): Uint8Array

external fun modPow(base: Uint8Array, exponent: Uint8Array, modulus: Uint8Array): Uint8Array

external fun modInverse(number: Uint8Array, modulus: Uint8Array): Uint8Array

external fun intoString(number: Uint8Array, radix: Int): String

external fun compare(number: Uint8Array, other: Uint8Array): Int

external fun getProbablePrime(size: Int): String

external interface InitOutput {
    var memory: dynamic
    var add: (a: Number, b: Number, c: Number, d: Number, e: Number) -> Unit
    var subtract: (a: Number, b: Number, c: Number, d: Number, e: Number) -> Unit
    var multiply: (a: Number, b: Number, c: Number, d: Number, e: Number) -> Unit
    var divide: (a: Number, b: Number, c: Number, d: Number, e: Number) -> Unit
    var remainder: (a: Number, b: Number, c: Number, d: Number, e: Number) -> Unit
    var gcd: (a: Number, b: Number, c: Number, d: Number, e: Number) -> Unit
    var shiftLeft: (a: Number, b: Number, c: Number, d: Number) -> Unit
    var shiftRight: (a: Number, b: Number, c: Number, d: Number) -> Unit
    var modPow: (a: Number, b: Number, c: Number, d: Number, e: Number, f: Number, g: Number) -> Unit
    var modInverse: (a: Number, b: Number, c: Number, d: Number, e: Number) -> Unit
    var intoString: (a: Number, b: Number, c: Number, d: Number) -> Unit
    var compare: (a: Number, b: Number, c: Number, d: Number) -> Number
    var __wbindgen_add_to_stack_pointer: (a: Number) -> Number
    var __wbindgen_malloc: (a: Number) -> Number
    var __wbindgen_realloc: (a: Number, b: Number, c: Number) -> Number
    var __wbindgen_free: (a: Number, b: Number) -> Unit
}

external fun initSync(module: ArrayBufferView): InitOutput

external fun initSync(module: ArrayBuffer): InitOutput

@JsName("default")
external fun init(module_or_path: Request = definedExternally): Promise<InitOutput>

@JsName("default")
external fun init(): Promise<InitOutput>

@JsName("default")
external fun init(module_or_path: String = definedExternally): Promise<InitOutput>

@JsName("default")
external fun init(module_or_path: URL = definedExternally): Promise<InitOutput>

@JsName("default")
external fun init(module_or_path: Response = definedExternally): Promise<InitOutput>

@JsName("default")
external fun init(module_or_path: ArrayBufferView = definedExternally): Promise<InitOutput>

@JsName("default")
external fun init(module_or_path: ArrayBuffer = definedExternally): Promise<InitOutput>

@JsName("default")
external fun init(module_or_path: Promise<Any /* Request | String | URL | Response | ArrayBufferView | ArrayBuffer | WebAssembly.Module */> = definedExternally): Promise<InitOutput>
