/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

#![allow(non_snake_case)]
use wasm_bindgen::prelude::*;
use crate::BigUIntArithmetic::*;
use std::str;
use js_sys::Uint8Array;

#[cfg(feature = "wee_alloc")]
#[global_allocator]
static ALLOC: wee_alloc::WeeAlloc = wee_alloc::WeeAlloc::INIT;

fn wrapResult(result: BigUint) -> Uint8Array {
    let resultVec = result.to_bytes_be();
    let resultArray = Uint8Array::new_with_length(resultVec.len().try_into().unwrap());
    resultArray.copy_from(resultVec.as_slice());

    return resultArray;
}

#[wasm_bindgen]
pub fn add(
    summand1: Uint8Array,
    summand2: Uint8Array
) -> Uint8Array {
    let result = _add(
        summand1.to_vec(),
        summand2.to_vec(),
    );
    
    return wrapResult(result);

}

#[wasm_bindgen]
pub fn subtract(
    minuend: Uint8Array,
    subtrahend: Uint8Array
) -> Uint8Array {
    let result = _subtract(
        minuend.to_vec(),
        subtrahend.to_vec(),
    );
    
    return wrapResult(result);
}

#[wasm_bindgen]
pub fn multiply(
    factor1: Uint8Array,
    factor2: Uint8Array
) -> Uint8Array {
    let result = _multiply(
        factor1.to_vec(),
        factor2.to_vec(),
    );
    
    return wrapResult(result);
}

#[wasm_bindgen]
pub fn divide(
    dividend: Uint8Array,
    divisor: Uint8Array
) -> Uint8Array {
    let result = _divide(
        dividend.to_vec(),
        divisor.to_vec(),
    );
    
    return wrapResult(result);
}

#[wasm_bindgen]
pub fn remainder(
    number: Uint8Array,
    modulus: Uint8Array
) -> Uint8Array {
    let result = _remainder(
        number.to_vec(),
        modulus.to_vec(),
    );
    
    return wrapResult(result);
}

#[wasm_bindgen]
pub fn gcd(
    number: Uint8Array,
    modulus: Uint8Array
) -> Uint8Array {
    let result = _gcd(
        number.to_vec(),
        modulus.to_vec(),
    );
    
    return wrapResult(result);
}

#[wasm_bindgen]
pub fn shiftLeft(
    number: Uint8Array,
    shifts: i64
) -> Uint8Array {
    let result = _shiftLeft(
        number.to_vec(),
        shifts,
    );
    
    return wrapResult(result);
}

#[wasm_bindgen]
pub fn shiftRight(
    number: Uint8Array,
    shifts: i64
) -> Uint8Array {
    let result = _shiftRight(
        number.to_vec(),
        shifts,
    );
    
    return wrapResult(result);
}

#[wasm_bindgen]
pub fn modPow(
    base: Uint8Array,
    exponent: Uint8Array,
    modulus: Uint8Array,
) -> Uint8Array {
    let result = _modPow(
        base.to_vec(),
        exponent.to_vec(),
        modulus.to_vec(),
    );
    
    return wrapResult(result);
}

#[wasm_bindgen]
pub fn modInverse(
    number: Uint8Array,
    modulus: Uint8Array,
) -> Uint8Array {
    let result = _modInverse(
        number.to_vec(),
        modulus.to_vec(),
    );

    return match result {
        Some(value) => wrapResult(value),
        None => Uint8Array::new_with_length(0),
    };
}

#[wasm_bindgen]
pub fn intoString(
    number: &[u8],
    radix: i32,
) -> String {
    _intoString(
        number.to_vec(),
        radix.try_into().unwrap(),
    )
}

#[wasm_bindgen]
pub fn compare(
    number: Uint8Array,
    other: Uint8Array,
) -> i32 {
    _compare(
        number.to_vec(),
        other.to_vec(),
    )
}

/*
#[wasm_bindgen]
pub fn getProbablePrime(size: i32) -> Uint8Array {
    let result = _getProbablePrime(size.try_into().unwrap());
    
    return wrapResult(result);
}*/
