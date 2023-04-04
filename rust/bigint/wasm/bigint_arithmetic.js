
let wasm;

const heap = new Array(32).fill(undefined);

heap.push(undefined, null, true, false);

function getObject(idx) { return heap[idx]; }

let heap_next = heap.length;

function dropObject(idx) {
    if (idx < 36) return;
    heap[idx] = heap_next;
    heap_next = idx;
}

function takeObject(idx) {
    const ret = getObject(idx);
    dropObject(idx);
    return ret;
}

const cachedTextDecoder = new TextDecoder('utf-8', { ignoreBOM: true, fatal: true });

cachedTextDecoder.decode();

let cachedUint8Memory0 = new Uint8Array();

function getUint8Memory0() {
    if (cachedUint8Memory0.byteLength === 0) {
        cachedUint8Memory0 = new Uint8Array(wasm.memory.buffer);
    }
    return cachedUint8Memory0;
}

function getStringFromWasm0(ptr, len) {
    return cachedTextDecoder.decode(getUint8Memory0().subarray(ptr, ptr + len));
}

function addHeapObject(obj) {
    if (heap_next === heap.length) heap.push(heap.length + 1);
    const idx = heap_next;
    heap_next = heap[idx];

    heap[idx] = obj;
    return idx;
}
/**
* @param {Uint8Array} summand1
* @param {Uint8Array} summand2
* @returns {Uint8Array}
*/
export function add(summand1, summand2) {
    const ret = wasm.add(addHeapObject(summand1), addHeapObject(summand2));
    return takeObject(ret);
}

/**
* @param {Uint8Array} minuend
* @param {Uint8Array} subtrahend
* @returns {Uint8Array}
*/
export function subtract(minuend, subtrahend) {
    const ret = wasm.subtract(addHeapObject(minuend), addHeapObject(subtrahend));
    return takeObject(ret);
}

/**
* @param {Uint8Array} factor1
* @param {Uint8Array} factor2
* @returns {Uint8Array}
*/
export function multiply(factor1, factor2) {
    const ret = wasm.multiply(addHeapObject(factor1), addHeapObject(factor2));
    return takeObject(ret);
}

/**
* @param {Uint8Array} dividend
* @param {Uint8Array} divisor
* @returns {Uint8Array}
*/
export function divide(dividend, divisor) {
    const ret = wasm.divide(addHeapObject(dividend), addHeapObject(divisor));
    return takeObject(ret);
}

/**
* @param {Uint8Array} number
* @param {Uint8Array} modulus
* @returns {Uint8Array}
*/
export function remainder(number, modulus) {
    const ret = wasm.remainder(addHeapObject(number), addHeapObject(modulus));
    return takeObject(ret);
}

/**
* @param {Uint8Array} number
* @param {Uint8Array} modulus
* @returns {Uint8Array}
*/
export function gcd(number, modulus) {
    const ret = wasm.gcd(addHeapObject(number), addHeapObject(modulus));
    return takeObject(ret);
}

/**
* @param {Uint8Array} number
* @param {bigint} shifts
* @returns {Uint8Array}
*/
export function shiftLeft(number, shifts) {
    const ret = wasm.shiftLeft(addHeapObject(number), shifts);
    return takeObject(ret);
}

/**
* @param {Uint8Array} number
* @param {bigint} shifts
* @returns {Uint8Array}
*/
export function shiftRight(number, shifts) {
    const ret = wasm.shiftRight(addHeapObject(number), shifts);
    return takeObject(ret);
}

/**
* @param {Uint8Array} base
* @param {Uint8Array} exponent
* @param {Uint8Array} modulus
* @returns {Uint8Array}
*/
export function modPow(base, exponent, modulus) {
    const ret = wasm.modPow(addHeapObject(base), addHeapObject(exponent), addHeapObject(modulus));
    return takeObject(ret);
}

/**
* @param {Uint8Array} number
* @param {Uint8Array} modulus
* @returns {Uint8Array}
*/
export function modInverse(number, modulus) {
    const ret = wasm.modInverse(addHeapObject(number), addHeapObject(modulus));
    return takeObject(ret);
}

let WASM_VECTOR_LEN = 0;

function passArray8ToWasm0(arg, malloc) {
    const ptr = malloc(arg.length * 1);
    getUint8Memory0().set(arg, ptr / 1);
    WASM_VECTOR_LEN = arg.length;
    return ptr;
}

let cachedInt32Memory0 = new Int32Array();

function getInt32Memory0() {
    if (cachedInt32Memory0.byteLength === 0) {
        cachedInt32Memory0 = new Int32Array(wasm.memory.buffer);
    }
    return cachedInt32Memory0;
}
/**
* @param {Uint8Array} number
* @param {number} radix
* @returns {string}
*/
export function intoString(number, radix) {
    try {
        const retptr = wasm.__wbindgen_add_to_stack_pointer(-16);
        const ptr0 = passArray8ToWasm0(number, wasm.__wbindgen_malloc);
        const len0 = WASM_VECTOR_LEN;
        wasm.intoString(retptr, ptr0, len0, radix);
        var r0 = getInt32Memory0()[retptr / 4 + 0];
        var r1 = getInt32Memory0()[retptr / 4 + 1];
        return getStringFromWasm0(r0, r1);
    } finally {
        wasm.__wbindgen_add_to_stack_pointer(16);
        wasm.__wbindgen_free(r0, r1);
    }
}

/**
* @param {Uint8Array} number
* @param {Uint8Array} other
* @returns {number}
*/
export function compare(number, other) {
    const ret = wasm.compare(addHeapObject(number), addHeapObject(other));
    return ret;
}

async function load(module, imports) {
    if (typeof Response === 'function' && module instanceof Response) {
        if (typeof WebAssembly.instantiateStreaming === 'function') {
            try {
                return await WebAssembly.instantiateStreaming(module, imports);

            } catch (e) {
                if (module.headers.get('Content-Type') != 'application/wasm') {
                    console.warn("`WebAssembly.instantiateStreaming` failed because your server does not serve wasm with `application/wasm` MIME type. Falling back to `WebAssembly.instantiate` which is slower. Original error:\n", e);

                } else {
                    throw e;
                }
            }
        }

        const bytes = await module.arrayBuffer();
        return await WebAssembly.instantiate(bytes, imports);

    } else {
        const instance = await WebAssembly.instantiate(module, imports);

        if (instance instanceof WebAssembly.Instance) {
            return { instance, module };

        } else {
            return instance;
        }
    }
}

function getImports() {
    const imports = {};
    imports.wbg = {};
    imports.wbg.__wbindgen_object_drop_ref = function(arg0) {
        takeObject(arg0);
    };
    imports.wbg.__wbg_buffer_3f3d764d4747d564 = function(arg0) {
        const ret = getObject(arg0).buffer;
        return addHeapObject(ret);
    };
    imports.wbg.__wbg_newwithbyteoffsetandlength_d9aa266703cb98be = function(arg0, arg1, arg2) {
        const ret = new Uint8Array(getObject(arg0), arg1 >>> 0, arg2 >>> 0);
        return addHeapObject(ret);
    };
    imports.wbg.__wbg_new_8c3f0052272a457a = function(arg0) {
        const ret = new Uint8Array(getObject(arg0));
        return addHeapObject(ret);
    };
    imports.wbg.__wbg_set_83db9690f9353e79 = function(arg0, arg1, arg2) {
        getObject(arg0).set(getObject(arg1), arg2 >>> 0);
    };
    imports.wbg.__wbg_length_9e1ae1900cb0fbd5 = function(arg0) {
        const ret = getObject(arg0).length;
        return ret;
    };
    imports.wbg.__wbg_newwithlength_f5933855e4f48a19 = function(arg0) {
        const ret = new Uint8Array(arg0 >>> 0);
        return addHeapObject(ret);
    };
    imports.wbg.__wbindgen_throw = function(arg0, arg1) {
        throw new Error(getStringFromWasm0(arg0, arg1));
    };
    imports.wbg.__wbindgen_memory = function() {
        const ret = wasm.memory;
        return addHeapObject(ret);
    };

    return imports;
}

function initMemory(imports, maybe_memory) {

}

function finalizeInit(instance, module) {
    wasm = instance.exports;
    init.__wbindgen_wasm_module = module;
    cachedInt32Memory0 = new Int32Array();
    cachedUint8Memory0 = new Uint8Array();


    return wasm;
}

function initSync(module) {
    const imports = getImports();

    initMemory(imports);

    if (!(module instanceof WebAssembly.Module)) {
        module = new WebAssembly.Module(module);
    }

    const instance = new WebAssembly.Instance(module, imports);

    return finalizeInit(instance, module);
}

async function init(input) {
    if (typeof input === 'undefined') {
        input = new URL('bigint_arithmetic_bg.wasm', import.meta.url);
    }
    const imports = getImports();

    if (typeof input === 'string' || (typeof Request === 'function' && input instanceof Request) || (typeof URL === 'function' && input instanceof URL)) {
        input = fetch(input);
    }

    initMemory(imports);

    const { instance, module } = await load(await input, imports);

    return finalizeInit(instance, module);
}

export { initSync }
export default init;
