package com.xicks.fibonacciserver.calculateFibonacci

import com.xicks.fibonacciserver.Fibonacci
import com.xicks.fibonacciserver.calculateFibonacci.gateways.FibonacciGateway
import com.xicks.fibonacciserver.repository.CrudRepository

class FibonacciGatewayImpl (
    private val postgresFibonacciRepository: CrudRepository<Int, Fibonacci>
) : FibonacciGateway {

    override suspend fun save(index: Int, value: Int) : Int = postgresFibonacciRepository.insert(Fibonacci(index, value))

    override suspend fun find(index: Int): Fibonacci? = postgresFibonacciRepository.find(index)

    override suspend fun findAll(): List<Fibonacci> = postgresFibonacciRepository.findAll()

}