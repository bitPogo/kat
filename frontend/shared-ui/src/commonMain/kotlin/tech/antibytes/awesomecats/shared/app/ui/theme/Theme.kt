/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

/* ktlint-disable filename */
package tech.antibytes.awesomecats.shared.app.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColors(
    primary = Color.White,
    primaryVariant = BrightGray,
    onPrimary = Color.Black,
    /*secondary = Teal200,
    secondaryVariant = Color.Magenta,
    background = Color.White,
    surface = Color.White,
    onSecondary = Color.Yellow,
    onBackground = Color.Red,
    onSurface = Color.DarkGray,*/
)

@Composable
fun AwesomeCatsTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}
