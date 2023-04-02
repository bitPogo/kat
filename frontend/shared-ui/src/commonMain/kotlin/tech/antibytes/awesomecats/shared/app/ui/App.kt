/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.shared.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import tech.antibytes.awesomecats.store.ViewModelContract

@Composable
fun App(
    viewModel: ViewModelContract,
    image: @Composable (String) -> Unit,
) {
    val cat = viewModel.cat.collectAsState()

    Column {
        Text("Purr Level: ${cat.value.purrLevel}")
        image(cat.value.url)
        Button(
            onClick = { viewModel.requestCat() },
        ) {
            Text("Get a new Cat")
        }
    }
}
