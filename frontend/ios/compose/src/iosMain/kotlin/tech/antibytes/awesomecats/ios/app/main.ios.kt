/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.ios.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Application
import com.seiko.imageloader.rememberAsyncImagePainter
import com.seiko.imageloader.ImageLoaderBuilder
import com.seiko.imageloader.LocalImageLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import platform.UIKit.UIViewController
import tech.antibytes.awesomecats.common.CAT_HOST
import tech.antibytes.awesomecats.shared.app.ui.App
import tech.antibytes.awesomecats.shared.app.ui.theme.AwesomeCatsTheme
import tech.antibytes.awesomecats.store.CatViewModel
import tech.antibytes.pixabay.sdk.ClientContract

fun MainViewController(): UIViewController {
    val viewModel = CatViewModel.getInstance(
        Logger,
        { true },
        42,
        { CoroutineScope(Dispatchers.Default) },
        { CoroutineScope(Dispatchers.Default) },
        CAT_HOST,
    )

    return Application("AwesomeCats") {
        AwesomeCatsTheme {
            Surface(modifier = Modifier.fillMaxSize().padding(top = 100.dp), color = MaterialTheme.colors.background) {
                App(viewModel) { url ->
                    AsyncImage(
                        url,
                        "",
                        Modifier.width(300.dp),
                    )
                }
            }
        }
    }
}

// Visibility is a factor
@Composable
private fun AsyncImage(
    url: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider(
        LocalImageLoader provides ImageLoaderBuilder().build(),
    ) {
        val resource = rememberAsyncImagePainter(
            url = url,
            imageLoader = LocalImageLoader.current,
        )

        Image(
            painter = resource,
            contentDescription = contentDescription,
            modifier = modifier,
        )
    }
}

private object Logger : ClientContract.Logger {
    override fun log(message: String) {
        println(message)
    }

    override fun error(exception: Throwable, message: String?) {
        println(message)
        println(exception)
    }

    override fun info(message: String) {
        println(message)
    }

    override fun warn(message: String) {
        println(message)
    }
}
