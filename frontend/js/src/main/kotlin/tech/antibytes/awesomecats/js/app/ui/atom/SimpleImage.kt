/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.js.app.ui.atom

import tech.antibytes.awesomecats.js.app.ui.react.render
import react.Component
import react.Key
import react.Props
import react.ReactNode
import react.State
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.img

external interface SimpleImageProps : Props {
    var url: String
}

fun SimpleImageProps() = object : SimpleImageProps {
    override var url = ""
    override var key: Key?
        get() = TODO("Not yet implemented")
        set(value) {
        }
}

class SimpleImage: Component<SimpleImageProps, State>(SimpleImageProps()) {
    override fun render(): ReactNode {
        return render {
            div {
                img {
                    src = props.url
                }
            }
        }
    }
}
