/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.backend.di

import tech.antibytes.awesomecats.backend.pixabay.domain.usecase.UsecaseContract
import tech.antibytes.util.test.isNot
import kotlin.test.Test

class KoinTest {
    @Test
    fun `It contains an Usecase`() {
        composeApplication().koin.getOrNull<UsecaseContract>() isNot null
    }
}
