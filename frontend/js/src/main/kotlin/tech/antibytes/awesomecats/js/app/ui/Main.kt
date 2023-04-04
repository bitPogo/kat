/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.js.app.ui

import browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import react.FC
import react.Props
import react.State
import react.create
import react.dom.client.createRoot
import react.dom.html.ReactHTML.div
import react.useState
import tech.antibytes.awesomecats.js.app.ui.atom.Divider
import tech.antibytes.awesomecats.js.app.ui.atom.SimpleButton
import tech.antibytes.awesomecats.js.app.ui.atom.SimpleImage
import tech.antibytes.awesomecats.js.app.ui.atom.SimpleText
import tech.antibytes.awesomecats.store.CatState
import tech.antibytes.awesomecats.store.CatStore
import tech.antibytes.pixabay.sdk.ClientContract

external interface AppState : State {
    val purrLevel: String
    val url: String
}

data class AppStateContainer(
    override val purrLevel: String,
    override val url: String,
) : AppState

val store = CatStore.getInstance(
    Logger,
    { true },
    42,
    { CoroutineScope(Dispatchers.Default) },
    { CoroutineScope(Dispatchers.Default) },
)

/*
class Application() : Component<Props, AppState>() {
    init {
        state = AppStateContainer(
            "0",
            "https://i.ytimg.com/vi/esxNJjOoTOQ/maxresdefault.jpg"
        )

        subscribeForCats()
    }

    private fun subscribeForCats() {
        viewModel.catState.subscribe {
            if (it is CatState.Accepted) {
                this.setState(
                    AppStateContainer(
                        it.value.purrLevel.toString(),
                        it.value.url
                    )
                )

                console.log(state)
            }
        }
    }

    private fun requestCat() {
        viewModel.requestACat()
    }

    override fun componentDidMount() {
        requestCat()
    }

    override fun render(): ReactNode {
        val pure = SimpleText()
        val image = SimpleImage()
        val divider = Divider("divider")
        val newCat = SimpleButton("Get a new Cat") {
            requestCat()
        }

        pure.props.text = state.purrLevel
        image.props.url = state.url

        return render {
            div {
                +pure.render()
                +image.render()
                +divider.render()
                +newCat.render()
            }
        }
    }
}*/

fun requestCat() {
    store.requestACat()
}

val rootComponent = FC<Props> {
    var appState by useState {
        AppStateContainer(
            "0",
            "https://i.ytimg.com/vi/esxNJjOoTOQ/maxresdefault.jpg",
        )
    }

    div {
        val pure = SimpleText()
        val image = SimpleImage()
        val divider = Divider("divider")
        val newCat = SimpleButton("Get a new Cat") {
            requestCat()
            store.catState.subscribe {
                if (it is CatState.Accepted) {
                    console.log(it.value.purrLevel)

                    appState = AppStateContainer(
                        it.value.purrLevel.toString(),
                        it.value.url,
                    )
                }
            }
        }

        pure.props.text = appState.purrLevel
        image.props.url = appState.url

        div {
            +pure.render()
            +image.render()
            +divider.render()
            +newCat.render()
        }
    }
}

private object Logger : ClientContract.Logger {
    override fun log(message: String) {
        console.log(message)
    }

    override fun error(exception: Throwable, message: String?) {
        console.error(message)
        console.error(exception)
    }

    override fun info(message: String) {
        console.info(message)
    }

    override fun warn(message: String) {
        console.warn(message)
    }
}

fun main() {
    document.getElementById("app")?.apply {
        val root = createRoot(this)
        root.render(rootComponent.create())
    }
}
