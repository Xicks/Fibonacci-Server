package com.xicks.fibonacciserver.calculateFibonacci.gateways

interface CachedFibonacciGateway {
    suspend fun save(index: Int, value: Int) : Int
    suspend fun find(index: Int) : Int?
}