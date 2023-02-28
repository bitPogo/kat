/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.backend

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import tech.antibytes.awesomecats.backend.di.composeApplication
import tech.antibytes.awesomecats.backend.pixabay.domain.usecase.UsecaseContract

private val koin = composeApplication()

fun main() {
    embeddedServer(CIO, host = "0.0.0.0", port = 8080, module = Application::myApplicationModule).start(wait = true)
}

fun Application.myApplicationModule() {
    routing {
        get("/") {
           runBlocking {
                koin.koin.get<UsecaseContract>().resolveCat().collectLatest { cat ->
                    call.respondText(cat.toString())
                }
            }
        }
    }
}
