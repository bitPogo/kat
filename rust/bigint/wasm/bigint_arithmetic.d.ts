/* tslint:disable */
/* eslint-disable */
/**
* @param {Uint8Array} summand1
* @param {Uint8Array} summand2
* @returns {Uint8Array}
*/
export function add(summand1: Uint8Array, summand2: Uint8Array): Uint8Array;
/**
* @param {Uint8Array} minuend
* @param {Uint8Array} subtrahend
* @returns {Uint8Array}
*/
export function subtract(minuend: Uint8Array, subtrahend: Uint8Array): Uint8Array;
/**
* @param {Uint8Array} factor1
* @param {Uint8Array} factor2
* @returns {Uint8Array}
*/
export function multiply(factor1: Uint8Array, factor2: Uint8Array): Uint8Array;
/**
* @param {Uint8Array} dividend
* @param {Uint8Array} divisor
* @returns {Uint8Array}
*/
export function divide(dividend: Uint8Array, divisor: Uint8Array): Uint8Array;
/**
* @param {Uint8Array} number
* @param {Uint8Array} modulus
* @returns {Uint8Array}
*/
export function remainder(number: Uint8Array, modulus: Uint8Array): Uint8Array;
/**
* @param {Uint8Array} number
* @param {Uint8Array} modulus
* @returns {Uint8Array}
*/
export function gcd(number: Uint8Array, modulus: Uint8Array): Uint8Array;
/**
* @param {Uint8Array} number
* @param {bigint} shifts
* @returns {Uint8Array}
*/
export function shiftLeft(number: Uint8Array, shifts: bigint): Uint8Array;
/**
* @param {Uint8Array} number
* @param {bigint} shifts
* @returns {Uint8Array}
*/
export function shiftRight(number: Uint8Array, shifts: bigint): Uint8Array;
/**
* @param {Uint8Array} base
* @param {Uint8Array} exponent
* @param {Uint8Array} modulus
* @returns {Uint8Array}
*/
export function modPow(base: Uint8Array, exponent: Uint8Array, modulus: Uint8Array): Uint8Array;
/**
* @param {Uint8Array} number
* @param {Uint8Array} modulus
* @returns {Uint8Array}
*/
export function modInverse(number: Uint8Array, modulus: Uint8Array): Uint8Array;
/**
* @param {Uint8Array} number
* @param {number} radix
* @returns {string}
*/
export function intoString(number: Uint8Array, radix: number): string;
/**
* @param {Uint8Array} number
* @param {Uint8Array} other
* @returns {number}
*/
export function compare(number: Uint8Array, other: Uint8Array): number;

export type InitInput = RequestInfo | URL | Response | BufferSource | WebAssembly.Module;

export interface InitOutput {
  readonly memory: WebAssembly.Memory;
  readonly add: (a: number, b: number) => number;
  readonly subtract: (a: number, b: number) => number;
  readonly multiply: (a: number, b: number) => number;
  readonly divide: (a: number, b: number) => number;
  readonly remainder: (a: number, b: number) => number;
  readonly gcd: (a: number, b: number) => number;
  readonly shiftLeft: (a: number, b: number) => number;
  readonly shiftRight: (a: number, b: number) => number;
  readonly modPow: (a: number, b: number, c: number) => number;
  readonly modInverse: (a: number, b: number) => number;
  readonly intoString: (a: number, b: number, c: number, d: number) => void;
  readonly compare: (a: number, b: number) => number;
  readonly __wbindgen_add_to_stack_pointer: (a: number) => number;
  readonly __wbindgen_malloc: (a: number) => number;
  readonly __wbindgen_free: (a: number, b: number) => void;
}

export type SyncInitInput = BufferSource | WebAssembly.Module;
/**
* Instantiates the given `module`, which can either be bytes or
* a precompiled `WebAssembly.Module`.
*
* @param {SyncInitInput} module
*
* @returns {InitOutput}
*/
export function initSync(module: SyncInitInput): InitOutput;

/**
* If `module_or_path` is {RequestInfo} or {URL}, makes a request and
* for everything else, calls `WebAssembly.instantiate` directly.
*
* @param {InitInput | Promise<InitInput>} module_or_path
*
* @returns {Promise<InitOutput>}
*/
export default function init (module_or_path?: InitInput | Promise<InitInput>): Promise<InitOutput>;
