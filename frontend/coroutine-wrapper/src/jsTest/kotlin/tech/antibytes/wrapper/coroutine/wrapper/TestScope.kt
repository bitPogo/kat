/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wrapper.coroutine.wrapper

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

actual val testScope1: CoroutineScope = MainScope()
actual val testScope2: CoroutineScope = MainScope()
