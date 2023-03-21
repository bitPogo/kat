/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store

import tech.antibytes.awesomecats.common.CAT_HOST
import tech.antibytes.awesomecats.store.model.FrontendCat
import tech.antibytes.pixabay.sdk.ClientContract
import tech.antibytes.wrapper.coroutine.result.State
import tech.antibytes.wrapper.coroutine.wrapper.CoroutineWrapperContract
import tech.antibytes.wrapper.coroutine.wrapper.CoroutineWrapperContract.SharedFlowWrapper

sealed class CatState : State {
    object Initial : CatState()
    object Pending : CatState()
    class Accepted(val value: FrontendCat) : CatState()
    class Error(val error: Throwable) : CatState()
}

interface CatStoreContract {
    val catState: SharedFlowWrapper<CatState>

    fun requestACat()
}


interface CatStoreFactoryContract {
    fun getInstance(
        logger: ClientContract.Logger,
        connection: ClientContract.ConnectivityManager,
        seed: Int,
        producerScope: CoroutineWrapperContract.CoroutineScopeDispatcher,
        consumerScope: CoroutineWrapperContract.CoroutineScopeDispatcher,
        host: String = CAT_HOST,
    ): CatStoreContract
}
