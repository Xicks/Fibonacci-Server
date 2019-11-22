package com.xicks.fibonacciserver.repository

interface CacheRepository<K,V> {
    suspend fun put(key: K, value: V)
    suspend fun get(key: K) : V?
}