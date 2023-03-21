/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.android.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import tech.antibytes.awesomecats.android.app.theme.AwesomeCatsTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import tech.antibytes.awesomecats.shared.app.ui.App
import tech.antibytes.awesomecats.shared.app.ui.ViewModelContract
import tech.antibytes.awesomecats.store.CatState
import tech.antibytes.awesomecats.store.CatStore
import tech.antibytes.awesomecats.store.CatStoreContract
import tech.antibytes.awesomecats.store.model.FrontendCat
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.height
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import tech.antibytes.pixabay.sdk.ClientContract

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = CatViewModel(
            CatStore.getInstance(
                Logger,
                { true },
                42,
                { CoroutineScope(Dispatchers.IO) },
                { CoroutineScope(Dispatchers.Default) },
                "10.0.2.2"
            )
        )

        setContent {
            AwesomeCatsTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    App(viewModel) { url ->
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(url)
                                .crossfade(true)
                                .scale(Scale.FILL)
                                .error(R.mipmap.placeholder_foreground)
                                .build(),
                            contentDescription = "",
                            placeholder = painterResource(R.mipmap.placeholder_foreground),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.height(240.dp),
                        )
                    }
                }
            }
        }
    }
}

private object Logger : ClientContract.Logger {
    override fun log(message: String) {
        println(message)
    }

    override fun error(exception: Throwable, message: String?) {
        System.err.println(message)
        System.err.println(exception)
    }

    override fun info(message: String) {
        println(message)
    }

    override fun warn(message: String) {
        println(message)
    }
}

val defaultCat = FrontendCat(
    "https://i.ytimg.com/vi/esxNJjOoTOQ/maxresdefault.jpg",
    0
)

class CatViewModel(
    private val store: CatStoreContract
) : ViewModelContract, ViewModel() {
    private val _cat: MutableStateFlow<FrontendCat> = MutableStateFlow(
        defaultCat
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
}
