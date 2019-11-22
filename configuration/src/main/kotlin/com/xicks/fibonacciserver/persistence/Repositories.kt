package com.xicks.fibonacciserver.persistence

import com.xicks.fibonacciserver.Fibonacci
import com.xicks.fibonacciserver.repository.CacheRepository
import com.xicks.fibonacciserver.repository.CrudRepository
import com.xicks.fibonacciserver.repository.PostgresFibonacciRepository
import com.xicks.fibonacciserver.repository.RedisRepository
import com.xicks.fibonacciserver.repository.StubbedCacheRepository
import com.xicks.fibonacciserver.repository.StubbedCrudRepository

object Repositories {
    val postgresFibonacciRepository: CrudRepository<Int, Fibonacci> = run {
        Connections.postgres?.let {
            PostgresFibonacciRepository(it)
        } ?: StubbedCrudRepository({1}) }

    val redisRepository: CacheRepository<String, String> = Connections.redis?.let {
        RedisRepository(Connections.redis, 86400L)
    } ?: StubbedCacheRepository()
}