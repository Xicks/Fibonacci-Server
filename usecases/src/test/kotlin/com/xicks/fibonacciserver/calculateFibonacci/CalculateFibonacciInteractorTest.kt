package com.xicks.fibonacciserver.calculateFibonacci

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CalculateFibonacciInteractorTest {

    private val retrieveInteractor: RetrieveFibonacciInteractor = mock()
    private val saveInteractor: SaveFibonacciInteractor = mock()
    private val interactor = CalculateFibonacciInteractor(retrieveInteractor, saveInteractor)

    @Test
    fun `should calculate fibonacci and save`() {
        runBlocking {
            whenever(retrieveInteractor.find(any())).thenReturn(null)
            val value = interactor.calculate(6)

            verify(saveInteractor).save(6, 8)
            Assertions.assertEquals(8, value)
        }
    }

    @Test
    fun `should cache fibonacci results`() {
        runBlocking {
            whenever(retrieveInteractor.find(any())).thenReturn(null)
            interactor.calculate(5)

            verify(saveInteractor, times(2)).cache(3, 2)
            verify(saveInteractor).cache(4, 3)
            verify(saveInteractor).cache(5, 5)
        }
    }

    @Test
    fun `should calculate fibonacci using cache`() {
        runBlocking {
            whenever(retrieveInteractor.find(5)).thenReturn(null)
            whenever(retrieveInteractor.find(4)).thenReturn(3)
            whenever(retrieveInteractor.find(3)).thenReturn(2)

            Assertions.assertEquals(5, interactor.calculate(5))
        }
    }
}