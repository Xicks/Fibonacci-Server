package com.xicks.fibonacciserver.calculateFibonacci

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.xicks.fibonacciserver.Fibonacci
import com.xicks.fibonacciserver.calculateFibonacci.gateways.CachedFibonacciGateway
import com.xicks.fibonacciserver.calculateFibonacci.gateways.FibonacciGateway
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RetrieveFibonacciInteractorTest {

    private val fibonacciGateway: FibonacciGateway = mock()
    private val cacheGateway: CachedFibonacciGateway = mock()
    private val interactor = RetrieveFibonacciInteractor(fibonacciGateway, cacheGateway)

    @Test
    fun `should find on database when cache returns null`() {
        runBlocking {
            whenever(cacheGateway.find(any())).thenReturn(null)
            whenever(fibonacciGateway.find(any())).thenReturn(Fibonacci(6, 8))
            val value = interactor.find(6)

            verify(cacheGateway).find(6)
            verify(fibonacciGateway).find(6)
            Assertions.assertEquals(8, value)
        }
    }
}