package com.xicks.fibonacciserver.calculateFibonacci

import com.xicks.fibonacciserver.calculateFibonacci.gateways.CachedFibonacciGateway
import com.xicks.fibonacciserver.repository.CacheRepository

class CachedFibonacciGatewayImpl (private val redisRepository: CacheRepository<String, String>) : CachedFibonacciGateway {

    companion object {
        private const val KEY_PREFIX = "fibonacci:"
    }

    override suspend fun save(index: Int, value: Int) : Int = redisRepository.put("$KEY_PREFIX$index", "$value").let { index }

    override suspend fun find(index: Int): Int? = redisRepository.get("$KEY_PREFIX$index")?.toIntOrNull()

}