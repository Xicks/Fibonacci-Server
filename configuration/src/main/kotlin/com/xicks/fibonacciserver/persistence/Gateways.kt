package com.xicks.fibonacciserver.persistence

import com.xicks.fibonacciserver.calculateFibonacci.CachedFibonacciGatewayImpl
import com.xicks.fibonacciserver.calculateFibonacci.FibonacciGatewayImpl
import com.xicks.fibonacciserver.health.PostgresHealthGatewayImpl
import com.xicks.fibonacciserver.health.RedisHealthGatewayImpl

object Gateways {
    val fibonacciGateway = FibonacciGatewayImpl(Repositories.postgresFibonacciRepository)
    val cachedfibonacciGateway = CachedFibonacciGatewayImpl(Repositories.redisRepository)
    val redisHealthGateway = RedisHealthGatewayImpl(Connections.redis)
    val postgresHealthGatewayImpl = PostgresHealthGatewayImpl(Connections.postgres)
}