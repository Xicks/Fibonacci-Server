package com.xicks.fibonacciserver

data class Health(val application: HealthStatus, val resources: List<ResourceHealth>)
data class ResourceHealth(val name: String, val status: HealthStatus)

enum class HealthStatus {
    UP,
    DOWN
}