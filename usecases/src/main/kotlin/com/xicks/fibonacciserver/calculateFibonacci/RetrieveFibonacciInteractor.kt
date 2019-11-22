package com.xicks.fibonacciserver.calculateFibonacci

import com.xicks.fibonacciserver.Fibonacci
import com.xicks.fibonacciserver.calculateFibonacci.gateways.CachedFibonacciGateway
import com.xicks.fibonacciserver.calculateFibonacci.gateways.FibonacciGateway

class RetrieveFibonacciInteractor (
    private val databaseGateway: FibonacciGateway,
    private val cachedGateway: CachedFibonacciGateway
) {
    suspend fun find(index: Int) : Int? = cachedGateway.find(index) ?: databaseGateway.find(index)?.value
    suspend fun findAll() : List<Fibonacci> = databaseGateway.findAll()
}