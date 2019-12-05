package com.xicks.fibonacciserver

import com.xicks.fibonacciserver.configuration.KtorConfiguration
import com.xicks.fibonacciserver.interactors.Interactors
import io.ktor.util.KtorExperimentalAPI

@KtorExperimentalAPI
fun main() = KtorConfiguration(
    Interactors.healthInteractor,
    Interactors.calculateFibonacciInteractor,
    Interactors.retrieveFibonacciInteractor,
    Environment.hoconConfig
).startServer()