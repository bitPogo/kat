/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

/* ktlint-disable filename */
package tech.antibytes.awesomecats.js.app.ui.react

import react.ChildrenBuilder
import react.Fragment
import react.create

fun render(content: ChildrenBuilder.() -> Unit) = Fragment.create(content)
