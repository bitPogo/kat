/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.awesomecats.common

import tech.antibytes.pixabay.sdk.ClientContract

class Logger(
    private val stdout: Function1<String, Unit>,
    private val stderr: Function1<String, Unit>,
) : ClientContract.Logger {
    override fun info(message: String) = stdout.invoke("INFO: $message")
    override fun log(message: String) = stdout.invoke("LOG: $message")
    override fun warn(message: String) = stderr.invoke("WARN: $message")
    override fun error(exception: Throwable, message: String?) {
        stderr.invoke("ERROR: $message")
        stderr.invoke("ERROR: ${exception.stackTraceToString()}")
    }
}
