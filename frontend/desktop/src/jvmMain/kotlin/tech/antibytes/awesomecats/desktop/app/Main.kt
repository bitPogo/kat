/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.desktop.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.res.loadXmlImageVector
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import java.io.File
import java.io.IOException
import java.net.URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.xml.sax.InputSource
import tech.antibytes.awesomecats.common.CAT_HOST
import tech.antibytes.awesomecats.shared.app.ui.App
import tech.antibytes.awesomecats.shared.app.ui.theme.AwesomeCatsTheme
import tech.antibytes.awesomecats.store.CatViewModel
import tech.antibytes.pixabay.sdk.ClientContract

fun main() = singleWindowApplication(
    title = "AwesomeCats",
    state = WindowState(width = 600.dp, height = 800.dp),
) {
    val viewModel = CatViewModel.getInstance(
        Logger,
        { true },
        42,
        { CoroutineScope(Dispatchers.IO) },
        { CoroutineScope(Dispatchers.Default) },
        CAT_HOST,
    )

    AwesomeCatsTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            App(viewModel) { url ->
                AsyncImage(
                    load = { loadImageBitmap(url) },
                    painterFor = { painter ->
                        BitmapPainter(painter)
                    },
                    contentDescription = "",
                    modifier = Modifier.width(500.dp),
                )
            }
        }
    }
}

@Composable
fun <T> AsyncImage(
    load: suspend () -> T,
    painterFor: @Composable (T) -> Painter,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val image: MutableState<T?> = remember { mutableStateOf(null) }

    CoroutineScope(Dispatchers.IO).launch {
        image.value = try {
            load()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    image.value?.let {
        Image(
            painter = painterFor(it),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier,
        )
    }
}

fun loadImageBitmap(file: File): ImageBitmap =
    file.inputStream().buffered().use(::loadImageBitmap)

fun loadSvgPainter(file: File, density: Density): Painter =
    file.inputStream().buffered().use { loadSvgPainter(it, density) }

fun loadXmlImageVector(file: File, density: Density): ImageVector =
    file.inputStream().buffered().use { loadXmlImageVector(InputSource(it), density) }

/* Loading from network with java.net API */

fun loadImageBitmap(url: String): ImageBitmap =
    URL(url).openStream().buffered().use(::loadImageBitmap)

fun loadSvgPainter(url: String, density: Density): Painter =
    URL(url).openStream().buffered().use { loadSvgPainter(it, density) }

fun loadXmlImageVector(url: String, density: Density): ImageVector =
    URL(url).openStream().buffered().use { loadXmlImageVector(InputSource(it), density) }

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
