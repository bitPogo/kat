/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wrapper.coroutine.wrapper

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import tech.antibytes.wrapper.coroutine.result.State

class SharedFlowWrapper<T : State> private constructor(
    private val flow: SharedFlow<T>,
    private val scope: CoroutineScope,
) : CoroutineWrapperContract.SharedFlowWrapper<T> {

    override val wrappedFlow: SharedFlow<T>
        get() = flow

    override val replayCache: List<T>
        get() = wrappedFlow.replayCache

    override fun subscribe(
        onEach: (item: T) -> Unit,
    ): Job = subscribeWithSuspendingFunction(onEach)

    override fun subscribeWithSuspendingFunction(
        onEach: suspend (item: T) -> Unit,
    ): Job {
        return wrappedFlow
            .onEach(onEach)
            .launchIn(scope)
    }

    companion object : CoroutineWrapperContract.SharedFlowWrapperFactory {
        override fun <T : State> getInstance(
            flow: SharedFlow<T>,
            dispatcher: CoroutineWrapperContract.CoroutineScopeDispatcher,
        ): CoroutineWrapperContract.SharedFlowWrapper<T> {
            return SharedFlowWrapper(
                flow,
                dispatcher.dispatch(),
            )
        }
    }
}
