package com.xicks.fibonacciserver.health

import com.xicks.fibonacciserver.health.gateways.HealthGateway
import io.lettuce.core.api.async.RedisAsyncCommands
import kotlinx.coroutines.future.await

class RedisHealthGatewayImpl (private val redis: RedisAsyncCommands<String, String>?) : HealthGateway {

    override val resourceName: String = "REDIS"

    override suspend fun ping(): Boolean  = runCatching { redis!!.echo("I am healthy").await() }.isSuccess

}