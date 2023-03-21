/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store

import tech.antibytes.awesomecats.store.model.FrontendCat

interface UseCaseContract {
    suspend fun findACat(): FrontendCat
}
