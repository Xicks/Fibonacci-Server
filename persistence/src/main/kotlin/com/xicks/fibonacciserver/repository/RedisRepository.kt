package com.xicks.fibonacciserver.repository

import io.lettuce.core.api.async.RedisAsyncCommands
import kotlinx.coroutines.future.await

class RedisRepository(
    private val redis: RedisAsyncCommands<String, String>,
    private val ttl: Long
) : CacheRepository<String, String> {

    override suspend fun put(key: String, value: String) { redis.setex(key, ttl, value).await() }

    override suspend fun get(key: String): String? = redis.get(key).await()

}