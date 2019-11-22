package com.xicks.fibonacciserver.health.gateways

interface HealthGateway {
    val resourceName: String
    suspend fun ping() : Boolean
}