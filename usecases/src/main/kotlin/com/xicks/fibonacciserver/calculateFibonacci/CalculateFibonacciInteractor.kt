package com.xicks.fibonacciserver.calculateFibonacci

class CalculateFibonacciInteractor (
    private val retrieveInteractor: RetrieveFibonacciInteractor,
    private val saveInteractor: SaveFibonacciInteractor
) {

    suspend fun calculate(index: Int) : Int = fibonacci(index).also { saveInteractor.save(index, it) }

    private suspend fun fibonacci(index: Int) : Int {
        if(index < 1) return 0
        if(index == 1) return 1

        val cached = retrieveInteractor.find(index)
        return cached ?: (fibonacci(index - 2) + fibonacci(index - 1)).also { saveInteractor.cache(index, it) }
    }

}