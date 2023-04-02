/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.ios.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Application
import kotlinx.cinterop.ObjCObjectBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.cinterop.autoreleasepool
import kotlinx.cinterop.cstr
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toCValues
import platform.Foundation.NSStringFromClass
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationDelegateProtocol
import platform.UIKit.UIApplicationDelegateProtocolMeta
import platform.UIKit.UIApplicationMain
import platform.UIKit.UIResponder
import platform.UIKit.UIResponderMeta
import platform.UIKit.UIScreen
import platform.UIKit.UIWindow
import tech.antibytes.awesomecats.common.CAT_HOST
import tech.antibytes.awesomecats.shared.app.ui.App
import tech.antibytes.awesomecats.shared.app.ui.theme.AwesomeCatsTheme
import tech.antibytes.awesomecats.store.CatViewModel
import tech.antibytes.pixabay.sdk.ClientContract
import com.seiko.imageloader.ImageLoaderBuilder
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.rememberAsyncImagePainter


fun main() {
    val args = emptyArray<String>()
    memScoped {
        val argc = args.size + 1
        val argv = (arrayOf("skikoApp") + args).map { it.cstr.ptr }.toCValues()
        autoreleasepool {
            UIApplicationMain(argc, argv, null, NSStringFromClass(AppDelegate))
        }
    }
}

class AppDelegate : UIResponder, UIApplicationDelegateProtocol {
    companion object : UIResponderMeta(), UIApplicationDelegateProtocolMeta

    @ObjCObjectBase.OverrideInit
    constructor() : super()

    private var _window: UIWindow? = null
    override fun window() = _window
    override fun setWindow(window: UIWindow?) {
        _window = window
    }

    override fun application(application: UIApplication, didFinishLaunchingWithOptions: Map<Any?, *>?): Boolean {
        val viewModel = CatViewModel.getInstance(
            Logger,
            { true },
            42,
            { CoroutineScope(Dispatchers.Default) },
            { CoroutineScope(Dispatchers.Default) },
            CAT_HOST,
        )
        window = UIWindow(frame = UIScreen.mainScreen.bounds)
        window!!.rootViewController = Application("AwesomeCats") {
            AwesomeCatsTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
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

        window!!.makeKeyAndVisible()
        return true
    }
}

@Composable
fun AsyncImage(
    url: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    val imageUrl: MutableState<String> = remember { mutableStateOf(url) }

    CompositionLocalProvider(
        LocalImageLoader provides ImageLoaderBuilder().build(),
    ) {
        val resource = rememberAsyncImagePainter(
            url = imageUrl.value,
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
