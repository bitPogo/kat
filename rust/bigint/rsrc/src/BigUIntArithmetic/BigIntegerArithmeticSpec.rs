#![allow(non_snake_case)]
use super::*;

#[test]
fn it_adds_ByteArrays() {
    // Given
    let summand1 = vec!(1, 42);
    let summand2 = vec!(1, 42);

    // When
    let actual = _add(summand1, summand2);

    // Then
    assert_eq!(
        actual.to_str_radix(10),
        "596"
    );
}

#[test]
fn it_substracts_ByteArrays() {
    // Given
    let minuend = vec!(1, 42);
    let subtrahend = vec!(1, 42);

    // When
    let actual = _subtract(minuend, subtrahend);

    // Then
    assert_eq!(
        actual.to_str_radix(10),
        "0"
    );
}

#[test]
fn it_multiplies_ByteArrays() {
    // Given
    let factor1 = vec!(1, 42);
    let factor2 = vec!(1, 42);

    // When
    let actual = _multiply(factor1, factor2);

    // Then
    assert_eq!(
        actual.to_str_radix(10),
        "88804"
    );
}

#[test]
fn it_divides_ByteArrays() {
    // Given
    let dividend = vec!(1, 90, 228);
    let divisor = vec!(1, 42);

    // When
    let actual = _divide(dividend, divisor);

    // Then
    assert_eq!(
        actual.to_str_radix(10),
        "298"
    );
}

#[test]
fn it_determines_the_remainder_from_ByteArrays() {
    // Given
    let number = vec!(1, 90, 228);
    let modulus = vec!(1, 42);

    // When
    let actual = _remainder(number, modulus);

    // Then
    assert_eq!(
        actual.to_str_radix(10),
        "0"
    );
}

#[test]
fn it_determines_the_greatest_common_divisor_from_ByteArrays() {
    // Given
    let number = vec!(1, 90, 228);
    let other = vec!(1, 42);

    // When
    let actual = _gcd(number, other);

    // Then
    assert_eq!(
        actual.to_str_radix(10),
        "298"
    );
}

#[test]
fn it_left_shifts_ByteArrays() {
    // Given
    let number = vec!(1, 90, 228);
    let shifts: i64 = 298;

    // When
    let actual = _shiftLeft(number, shifts);

    // Then
    assert_eq!(
        actual.to_str_radix(10),
        "45224235710601925601245762728376604553503649807777450700372954116116619236045838213977605144576"
    );
}

#[test]
fn it_right_shifts_ByteArrays() {
    // Given
    let number = vec!(1, 90, 228);
    let shifts: i64 = 10;

    // When
    let actual = _shiftRight(number, shifts);

    // Then
    assert_eq!(
        actual.to_str_radix(10),
        "86"
    );
}

#[test]
fn it_pows_while_determine_the_remainder_from_ByteArrays() {
    // Given
    let base = vec!(1, 90, 228);
    let exponent = vec!(1, 42);
    let modulus = vec!(1, 23);

    // When
    let actual = _modPow(base, exponent, modulus);

    // Then
    assert_eq!(
        actual.to_str_radix(10),
        "10"
    );
}

#[test]
fn it_determines_the_multiplicative_inverse() {
    // Given
    let number = vec!(1);
    let modulus = vec!(3);

    // When
    let actual = _modInverse(number, modulus).unwrap();

    // Then
    assert_eq!(
        actual.to_str_radix(10),
        "1"
    );
}

#[test]
fn it_resolves_multiplicative_inverse_to_none_if_there_no_inverse() {
    // Given
    let number = vec!(1, 90, 228);
    let modulus = vec!(1, 42);

    // When
    let actual = _modInverse(number, modulus);

    // Then
    assert_eq!(
        actual,
        None
    );
}

#[test]
fn it_returns_its_value_to_a_given_radix() {
    // Given
    let number = vec!(1, 42);

    // When
    let actual = _intoString(number, 10);

    // Then
    assert_eq!(
        actual,
        "298"
    );
}

#[test]
fn it_returns_a_negative_int_if_number1_is_smaller_than_number2() {
    // Given
    let number1 = vec!(1, 42);
    let number2 = vec!(1, 43);

    // When
    let actual = _compare(number1, number2);

    // Then
    assert_eq!(
        actual,
        -1
    );
}

#[test]
fn it_returns_a_positve_int_if_number1_is_greater_than_number2() {
    // Given
    let number1 = vec!(1, 43);
    let number2 = vec!(1, 42);

    // When
    let actual = _compare(number1, number2);

    // Then
    assert_eq!(
        actual,
        1
    );
}

#[test]
fn it_returns_zero_int_if_number1_equals_number2() {
    // Given
    let number1 = vec!(1, 42);
    let number2 = vec!(1, 42);

    // When
    let actual = _compare(number1, number2);

    // Then
    assert_eq!(
        actual,
        0
    );
}

#[test]
fn it_generates_a_prime() {
    // When
    let number = _getProbablePrime(128);
    let len = number.to_str_radix(10).len();

    // Then
    assert_ne!(
        len,
        0
    )
}
