/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.android.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import tech.antibytes.awesomecats.common.CAT_HOST
import tech.antibytes.awesomecats.shared.app.ui.App
import tech.antibytes.awesomecats.shared.app.ui.theme.AwesomeCatsTheme
import tech.antibytes.awesomecats.store.CatViewModel
import tech.antibytes.pixabay.sdk.ClientContract

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = CatViewModel.getInstance(
            Logger,
            { true },
            42,
            { CoroutineScope(Dispatchers.IO) },
            { CoroutineScope(Dispatchers.Default) },
            "10.0.2.2",
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
