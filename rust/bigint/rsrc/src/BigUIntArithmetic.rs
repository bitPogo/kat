#![allow(non_snake_case)]
use num_bigint_dig::{BigUint, RandPrime};
use std::ops::Add;
use std::ops::Sub;
use std::ops::Mul;
use std::ops::Div;
use std::ops::Rem;
use std::ops::Shl;
use std::ops::Shr;
use num_integer::Integer;
use num_bigint_dig::ModInverse;

fn toBigUint(number: Vec<u8>) -> BigUint {
    return BigUint::from_bytes_be(number.as_slice());
}

pub fn _add(
    summand1: Vec<u8>,
    summand2: Vec<u8>,
) -> BigUint {
    let bigSummand1 = toBigUint(summand1);
    let bigSummand2 = toBigUint(summand2);

    return bigSummand1.add(bigSummand2);
}

pub fn _subtract(
    minuend: Vec<u8>,
    subtrahend: Vec<u8>,
) -> BigUint {
    let bigMinuend = toBigUint(minuend);
    let bigSubtrahend = toBigUint(subtrahend);

    return bigMinuend.sub(bigSubtrahend);
}

pub fn _multiply(
    factor1: Vec<u8>,
    factor2: Vec<u8>,
) -> BigUint {
    let bigFactor1 = toBigUint(factor1);
    let bigFactor2 = toBigUint(factor2);

    return bigFactor1.mul(bigFactor2);
}

pub fn _divide(
    dividend: Vec<u8>,
    divisor: Vec<u8>,
) -> BigUint {
    let bigDividend = toBigUint(dividend);
    let bigDivisor = toBigUint(divisor);

    return bigDividend.div(bigDivisor);
}

pub fn _remainder(
    number: Vec<u8>,
    modulus: Vec<u8>,
) -> BigUint {
    let bigNumber = toBigUint(number);
    let bigModulus = toBigUint(modulus);

    return bigNumber.rem(bigModulus);
}

pub fn _gcd(
    number: Vec<u8>,
    other: Vec<u8>,
) -> BigUint {
    let bigNumber = toBigUint(number);
    let bigOther = toBigUint(other);

    return bigNumber.gcd(&bigOther);
}

pub fn _shiftLeft(
    number: Vec<u8>,
    shifts: i64,
) -> BigUint {
    let bigNumber = toBigUint(number);

    return bigNumber.shl(shifts.try_into().unwrap());
}

pub fn _shiftRight(
    number: Vec<u8>,
    shifts: i64,
) -> BigUint {
    let bigNumber = toBigUint(number);

    return bigNumber.shr(shifts.try_into().unwrap());
}

pub fn _modPow(
    base: Vec<u8>,
    exponent: Vec<u8>,
    modulus: Vec<u8>,
) -> BigUint {
    let bigBase = toBigUint(base);
    let bigExponent = toBigUint(exponent);
    let bigModulus = toBigUint(modulus);

    return bigBase.modpow(&bigExponent, &bigModulus);
}

pub fn _modInverse(
    number: Vec<u8>,
    modulus: Vec<u8>,
) -> Option<BigUint> {
    let bigNumber = toBigUint(number);
    let bigModulus = toBigUint(modulus);

    let result = bigNumber.mod_inverse(&bigModulus);

    return match result {
        Some(value) => Some(value.to_biguint().unwrap()),
        None => None
    };

}

pub fn _intoString(
    number: Vec<u8>,
    radix: u32,
) -> String {
    return BigUint::from_bytes_be(number.as_slice()).to_str_radix(radix)
}

pub fn _compare(
    number: Vec<u8>,
    other: Vec<u8>,
) -> i32 {
    let number1 = toBigUint(number);
    let number2 = toBigUint(other);

    return if number1 < number2 {
        -1
    } else if number1 > number2 {
        1
    } else {
        0
    };
}

/*pub fn _getProbablePrime(
    size: usize
) -> BigUint {
    return rand::thread_rng().gen_prime(size);
}*/

#[cfg(target_family="wasm")]
mod WASM;
#[cfg(all(not(target_family="wasm")))]//, feature="jvm"))] // FIXME
// mod Jvm;
mod Native;
// #[cfg(all(not(target_family="wasm"), feature="native"))] // FIXME
// mod Native
#[cfg(test)]
mod BigIntegerArithmeticSpec;
