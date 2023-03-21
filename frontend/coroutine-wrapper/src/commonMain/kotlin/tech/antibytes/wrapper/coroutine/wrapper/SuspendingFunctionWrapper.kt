/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wrapper.coroutine.wrapper

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SuspendingFunctionWrapper<T> private constructor(
    override val wrappedFunction: suspend () -> T,
    private val scope: CoroutineScope,
) : CoroutineWrapperContract.SuspendingFunctionWrapper<T> {

    override fun subscribe(
        onSuccess: (item: T) -> Unit,
        onError: (error: Throwable) -> Unit,
    ): Job {
        return scope.launch {
            try {
                val result = wrappedFunction.invoke()
                onSuccess(result)
            } catch (error: Throwable) {
                onError(error)
            }
        }
    }

    companion object Factory : CoroutineWrapperContract.SuspendingFunctionWrapperFactory {
        override fun <T> getInstance(
            function: suspend () -> T,
            dispatcher: CoroutineWrapperContract.CoroutineScopeDispatcher,
        ): SuspendingFunctionWrapper<T> {
            return SuspendingFunctionWrapper(
                function,
                dispatcher.dispatch(),
            )
        }
    }
}
