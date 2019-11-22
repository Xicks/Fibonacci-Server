package com.xicks.fibonacciserver.interactors

import com.xicks.fibonacciserver.calculateFibonacci.CalculateFibonacciInteractor
import com.xicks.fibonacciserver.calculateFibonacci.RetrieveFibonacciInteractor
import com.xicks.fibonacciserver.calculateFibonacci.SaveFibonacciInteractor
import com.xicks.fibonacciserver.persistence.Gateways
import com.xicks.fibonacciserver.health.HealthInteractor

object Interactors {
    val healthInteractor = HealthInteractor(listOf(Gateways.postgresHealthGatewayImpl, Gateways.redisHealthGateway))
    val retrieveFibonacciInteractor = RetrieveFibonacciInteractor(Gateways.fibonacciGateway, Gateways.cachedfibonacciGateway)
    val saveFibonacciInteractor = SaveFibonacciInteractor(Gateways.fibonacciGateway, Gateways.cachedfibonacciGateway)
    val calculateFibonacciInteractor = CalculateFibonacciInteractor(retrieveFibonacciInteractor, saveFibonacciInteractor)
}