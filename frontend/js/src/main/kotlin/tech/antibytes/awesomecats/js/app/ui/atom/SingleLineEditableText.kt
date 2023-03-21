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
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label

external interface SingleLineEditableTextProps : Props {
    var value: String
}

private fun SingleLineEditableTextProps(): SingleLineEditableTextProps {
    return object : SingleLineEditableTextProps {
        override var value: String = ""
        override var key: Key?
            get() = TODO("Not yet implemented")
            set(_) {}
    }
}

class SingleLineEditableText(
    private val label: String,
    private val onChange: Function1<dynamic, Unit>,
) : Component<SingleLineEditableTextProps, State>(SingleLineEditableTextProps()) {
    override fun render(): ReactNode? {
        return render {
            div {
                label {
                    +this@SingleLineEditableText.label
                }
                input {
                    this.onInput = this@SingleLineEditableText.onChange
                }
            }
        }
    }
}
