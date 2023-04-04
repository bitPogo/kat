/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.store

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import tech.antibytes.awesomecats.store.model.FrontendCat
import tech.antibytes.awesomecats.store.viewmodel.ViewModel
import tech.antibytes.pixabay.sdk.ClientContract
import tech.antibytes.wrapper.coroutine.wrapper.CoroutineWrapperContract

val defaultCat = FrontendCat(
    "https://i.ytimg.com/vi/esxNJjOoTOQ/maxresdefault.jpg",
    "0",
)

class CatViewModel internal constructor(
    private val store: CatStoreContract,
) : ViewModelContract, ViewModel() {
    private val _cat: MutableStateFlow<FrontendCat> = MutableStateFlow(
        defaultCat,
    )
    override val cat: StateFlow<FrontendCat> = _cat

    init {
        store.catState.subscribe { state -> evaluateState(state) }
    }

    private fun evaluateState(state: CatState) {
        if (state is CatState.Accepted) {
            _cat.update { state.value.copy() }
        }
    }

    override fun requestCat() = store.requestACat()

    companion object : ViewModelFactoryContract {
        override fun getInstance(
            logger: ClientContract.Logger,
            connection: ClientContract.ConnectivityManager,
            seed: Int,
            producerScope: CoroutineWrapperContract.CoroutineScopeDispatcher,
            consumerScope: CoroutineWrapperContract.CoroutineScopeDispatcher,
            host: String,
        ): ViewModelContract = CatViewModel(
            CatStore.getInstance(
                logger = logger,
                connection = connection,
                seed = seed,
                producerScope = producerScope,
                consumerScope = consumerScope,
                host = host,
            ),
        )
    }
}
