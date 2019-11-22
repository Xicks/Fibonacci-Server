package com.xicks.fibonacciserver.calculateFibonacci

import com.xicks.fibonacciserver.calculateFibonacci.gateways.CachedFibonacciGateway
import com.xicks.fibonacciserver.calculateFibonacci.gateways.FibonacciGateway

class SaveFibonacciInteractor (
    private val databaseGateway: FibonacciGateway,
    private val cachedGateway: CachedFibonacciGateway
) {

    suspend fun save(index: Int, value: Int) = databaseGateway.save(index, value)
    suspend fun cache(index: Int, value: Int) = cachedGateway.save(index, value)

}