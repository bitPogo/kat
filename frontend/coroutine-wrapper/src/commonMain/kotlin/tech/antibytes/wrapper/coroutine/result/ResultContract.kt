/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wrapper.coroutine.result

interface State

sealed interface ResultContract<Success, Error : Throwable> : State {
    val value: Success?
    val error: Error?

    fun isSuccess(): Boolean
    fun isError(): Boolean

    @Throws(Throwable::class)
    fun unwrap(): Success
}
