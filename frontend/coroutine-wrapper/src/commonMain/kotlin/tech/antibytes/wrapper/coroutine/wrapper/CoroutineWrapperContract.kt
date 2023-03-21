/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wrapper.coroutine.wrapper

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import tech.antibytes.wrapper.coroutine.result.State

interface CoroutineWrapperContract {
    fun interface CoroutineScopeDispatcher {
        fun dispatch(): CoroutineScope
    }

    interface SuspendingFunctionWrapper<T> {
        val wrappedFunction: suspend () -> T

        fun subscribe(
            onSuccess: (item: T) -> Unit,
            onError: (error: Throwable) -> Unit,
        ): Job
    }

    interface SuspendingFunctionWrapperFactory {
        fun <T> getInstance(
            function: suspend () -> T,
            dispatcher: CoroutineScopeDispatcher,
        ): SuspendingFunctionWrapper<T>
    }

    interface SharedFlowWrapper<T : State> {
        val wrappedFlow: SharedFlow<T>
        val replayCache: List<T>

        fun subscribe(
            onEach: (item: T) -> Unit,
        ): Job

        fun subscribeWithSuspendingFunction(
            onEach: suspend (item: T) -> Unit,
        ): Job
    }

    interface SharedFlowWrapperFactory {
        fun <T : State> getInstance(
            flow: SharedFlow<T>,
            dispatcher: CoroutineScopeDispatcher,
        ): SharedFlowWrapper<T>
    }
}
