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
import react.dom.html.ReactHTML.p

external interface SimpleTextProps : Props {
    var text: String
}

fun SimpleTextProps() = object : SimpleTextProps {
    override var text = ""
    override var key: Key?
        get() = TODO("Not yet implemented")
        set(value) {
        }
}

class SimpleText : Component<SimpleTextProps, State>(SimpleTextProps()) {
    override fun render(): ReactNode {
        return render {
            div {
                p {
                    +"Purr Level: ${this@SimpleText.props.text}"
                }
            }
        }
    }
}
