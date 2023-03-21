/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.shared.app.ui

import kotlinx.coroutines.flow.StateFlow
import tech.antibytes.awesomecats.store.model.FrontendCat

interface ViewModelContract {
    val cat: StateFlow<FrontendCat>

    fun requestCat()
}
