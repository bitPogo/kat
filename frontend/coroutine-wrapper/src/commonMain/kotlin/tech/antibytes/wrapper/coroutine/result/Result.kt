/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wrapper.coroutine.result

data class Success<Succ, Err : Throwable>(
    override val value: Succ,
) : ResultContract<Succ, Err> {
    override val error: Err? = null

    override fun isSuccess(): Boolean = true

    override fun isError(): Boolean = false

    override fun unwrap(): Succ = value
}

data class Failure<Succ, Err : Throwable>(
    override val error: Err,
) : ResultContract<Succ, Err> {
    override val value: Succ? = null

    override fun isSuccess(): Boolean = false

    override fun isError(): Boolean = true

    override fun unwrap(): Succ = throw error
}
