/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.js.app.ui.atom

import tech.antibytes.awesomecats.js.app.ui.react.render
import org.w3c.dom.Element
import react.Component
import react.Props
import react.ReactNode
import react.State
import react.dom.events.MouseEvent
import react.dom.events.NativeMouseEvent
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div

class SimpleButton(
    private val label: String,
    private val onClick: (MouseEvent<Element, NativeMouseEvent>) -> Unit,
) : Component<Props, State>() {
    override fun render(): ReactNode {
        return render {
            div {
                button {
                    this.onClick = this@SimpleButton.onClick
                    +label
                }
            }
        }
    }
}
