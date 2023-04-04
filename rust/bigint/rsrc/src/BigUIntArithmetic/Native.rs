/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

#![allow(non_snake_case)]
use std::ffi::{CStr, CString};
use std::os::raw::{c_char, c_int, c_long};
use crate::BigUIntArithmetic::*;

fn wrapResult(result: BigUint) -> *mut c_char {
    let resultVec = result.to_bytes_be();
    return unsafe { CString::from_vec_unchecked(resultVec).into_raw() }
}

fn strToVec(input: *const c_char) -> Vec<u8> {
    let input = unsafe { CStr::from_ptr(input) };
    return input.to_str().unwrap().as_bytes().to_vec()
}

#[no_mangle]
pub extern "C" fn addNative(
    summand1: *const c_char,
    summand2: *const c_char
) -> *mut c_char {
    let result = _add(
        strToVec(summand1),
        strToVec(summand2),
    );

    return wrapResult(result)
}

#[no_mangle]
pub extern "C" fn subtractNative(
    minuend: *const c_char,
    subtrahend: *const c_char
) -> *const c_char {
    let result = _subtract(
        strToVec(minuend),
        strToVec(subtrahend),
    );
    
    return wrapResult(result);
}

#[no_mangle]
pub extern "C" fn multiplyNative(
    factor1: *const c_char,
    factor2: *const c_char
) -> *const c_char {
    let result = _multiply(
        strToVec(factor1),
        strToVec(factor2),
    );
    
    return wrapResult(result);
}

#[no_mangle]
pub extern "C"  fn divideNative(
    dividend: *const c_char,
    divisor: *const c_char
) -> *const c_char {
    let result = _divide(
        strToVec(dividend),
        strToVec(divisor),
    );
    
    return wrapResult(result);
}

#[no_mangle]
pub extern "C"  fn remainderNative(
    number: *const c_char,
    modulus: *const c_char
) -> *const c_char {
    let result = _remainder(
        strToVec(number),
        strToVec(modulus),
    );
    
    return wrapResult(result);
}

#[no_mangle]
pub extern "C"  fn gcdNative(
    number: *const c_char,
    modulus: *const c_char
) -> *const c_char {
    let result = _gcd(
        strToVec(number),
        strToVec(modulus),
    );
    
    return wrapResult(result);
}

#[no_mangle]
pub extern "C"  fn shiftLeftNative(
    number: *const c_char,
    shifts: c_long
) -> *const c_char {
    let result = _shiftLeft(
        strToVec(number),
        shifts.try_into().unwrap(),
    );
    
    return wrapResult(result);
}

#[no_mangle]
pub extern "C"  fn shiftRightNative(
    number: *const c_char,
    shifts: i64
) -> *const c_char {
    let result = _shiftRight(
        strToVec(number),
        shifts.try_into().unwrap(),
    );
    
    return wrapResult(result);
}

#[no_mangle]
pub extern "C"  fn modPowNative(
    base: *const c_char,
    exponent: *const c_char,
    modulus: *const c_char,
) -> *const c_char {
    let result = _modPow(
        strToVec(base),
        strToVec(exponent),
        strToVec(modulus),
    );
    
    return wrapResult(result);
}

#[no_mangle]
pub extern "C"  fn modInverseNative(
    number: *const c_char,
    modulus: *const c_char,
) -> *const c_char {
    let result = _modInverse(
        strToVec(number),
        strToVec(modulus),
    );

    match result {
        Some(value) => wrapResult(value),
        None =>  CString::new("").unwrap().into_raw(),
    }
}

#[no_mangle]
pub extern "C"  fn intoStringNative(
    number: *const c_char,
    radix: c_int,
) -> *const c_char {
    CString::new(
        _intoString(
            strToVec(number),
            radix.try_into().unwrap(),
        )
    ).unwrap().into_raw()
}

#[no_mangle]
pub extern "C" fn compareNative(
    number: *const c_char,
    other: *const c_char,
) -> c_int {
    _compare(
        strToVec(number),
        strToVec(other),
    ).try_into().unwrap()
}

/*
#[no_mangle]
pub extern "C"  fn getProbablePrimeNative(size: i32) -> *const c_char {
    let result = _getProbablePrime(size.try_into().unwrap());
    
    return wrapResult(result);
}*/

#[no_mangle]
pub extern "C" fn freeString(string: *mut c_char) {
    let cstring = unsafe { CString::from_raw(string) };
    drop(cstring);
}
