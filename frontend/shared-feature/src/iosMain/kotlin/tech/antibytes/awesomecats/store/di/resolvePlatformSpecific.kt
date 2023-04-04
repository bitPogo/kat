/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store.di

import io.bitpogo.krump.bignumber.BigUIntegerContract
import io.bitpogo.krump.bignumber.BigUIntegerFactory
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun resolvePlatformSpecific(): Module {
    return module {
        single<BigUIntegerContract.BigUIntegerFactory> {
            BigUIntegerFactory()
        }
    }
}