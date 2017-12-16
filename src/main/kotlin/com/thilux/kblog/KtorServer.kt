package com.thilux.kblog

/**
 * Created by tsantana on 15/12/17.
 */

import io.ktor.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val LOG: Logger = LoggerFactory.getLogger("ktor-server")

fun main(args: Array<String>) {

    LOG.debug("Starting ktor server.")

    embeddedServer(Netty, 1987, module = Application::main).start(wait = true)


}