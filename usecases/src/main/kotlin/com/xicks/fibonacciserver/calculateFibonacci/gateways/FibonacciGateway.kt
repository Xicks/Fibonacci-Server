package com.xicks.fibonacciserver.calculateFibonacci.gateways

import com.xicks.fibonacciserver.Fibonacci

interface FibonacciGateway {
    suspend fun save(index: Int, value: Int) : Int
    suspend fun find(index: Int) : Fibonacci?
    suspend fun findAll(): List<Fibonacci>
}