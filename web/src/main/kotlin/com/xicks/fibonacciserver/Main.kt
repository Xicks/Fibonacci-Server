package com.xicks.fibonacciserver

import com.xicks.fibonacciserver.configuration.KtorConfiguration
import io.ktor.util.KtorExperimentalAPI

@KtorExperimentalAPI
fun main() = KtorConfiguration.startServer()