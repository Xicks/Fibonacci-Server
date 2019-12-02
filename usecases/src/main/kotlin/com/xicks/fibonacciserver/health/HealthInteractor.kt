package com.xicks.fibonacciserver.health

import com.xicks.fibonacciserver.Health
import com.xicks.fibonacciserver.HealthStatus
import com.xicks.fibonacciserver.ResourceHealth
import com.xicks.fibonacciserver.health.gateways.HealthGateway

class HealthInteractor(private val gateways: List<HealthGateway>) {

    suspend fun isHealthy() : Health {
        val resources = gateways.map {
            try {
                ResourceHealth(it.resourceName, it.ping().toHealthStatus())
            } catch (_: Exception) {
                ResourceHealth(it.resourceName, HealthStatus.DOWN)
            }
        }
        val healthy = resources.all { (_, status) -> status == HealthStatus.UP }

        return Health(healthy.toHealthStatus(), resources)
    }

}

private fun Boolean.toHealthStatus(): HealthStatus = if(this) HealthStatus.UP else HealthStatus.DOWN