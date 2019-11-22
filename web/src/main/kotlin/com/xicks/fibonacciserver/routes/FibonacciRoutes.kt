package com.xicks.fibonacciserver.routes

import com.xicks.fibonacciserver.interactors.Interactors
import com.xicks.fibonacciserver.models.FibonacciCalculationResponse
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get

fun Routing.fibonacciRoutes() {
    val calculationInteractor = Interactors.calculateFibonacciInteractor
    val retrieveFibonacciInteractor = Interactors.retrieveFibonacciInteractor

    get("/fib/{index}") {
        val index = call.parameters["index"]?.toIntOrNull()

        if(index != null) {
            call.respond(FibonacciCalculationResponse(index, calculationInteractor.calculate(index)))
        } else {
            call.respond(HttpStatusCode.BadRequest)
        }

    }

    get("/fib") {
        val results = retrieveFibonacciInteractor.findAll().map {
            FibonacciCalculationResponse(it.index, it.value)
        }
        call.respond(results)
    }

}