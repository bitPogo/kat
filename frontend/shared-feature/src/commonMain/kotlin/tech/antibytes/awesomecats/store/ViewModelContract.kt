/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store

import kotlinx.coroutines.flow.StateFlow
import tech.antibytes.awesomecats.common.CAT_HOST
import tech.antibytes.awesomecats.store.model.FrontendCat
import tech.antibytes.pixabay.sdk.ClientContract
import tech.antibytes.wrapper.coroutine.wrapper.CoroutineWrapperContract

interface ViewModelContract {
    val cat: StateFlow<FrontendCat>

    fun requestCat()
}

interface ViewModelFactoryContract {
    fun getInstance(
        logger: ClientContract.Logger,
        connection: ClientContract.ConnectivityManager,
        seed: Int,
        producerScope: CoroutineWrapperContract.CoroutineScopeDispatcher,
        consumerScope: CoroutineWrapperContract.CoroutineScopeDispatcher,
        host: String = CAT_HOST,
    ): ViewModelContract
}
