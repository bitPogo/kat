/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.backend

import io.ktor.http.ContentType
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.util.pipeline.PipelineContext
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import tech.antibytes.awesomecats.backend.di.composeApplication
import tech.antibytes.awesomecats.backend.pixabay.domain.usecase.UsecaseContract
import tech.antibytes.awesomecats.common.CAT_HOST
import tech.antibytes.awesomecats.common.CAT_PORT

private val koin = composeApplication()

fun main() {
    embeddedServer(CIO, host = CAT_HOST, port = CAT_PORT) {
        addCORS()
        route()
    }.start(wait = true)
}

fun Application.route() = routing {
    routeRoot()
}

fun Application.addCORS() {
    install(DefaultHeaders) {
        header("Access-Control-Allow-Origin", "*")
    }
}

private fun Route.routeRoot() {
    get("/") {
        resolvePlainCat()
    }
}

private fun PipelineContext<Unit, ApplicationCall>.resolvePlainCat() = runBlocking {
    koin.koin.get<UsecaseContract>().resolveCat().collectLatest { cat ->
        call.respondText(
            cat.toString(),
            ContentType.Application.Json,
        )
    }
}
