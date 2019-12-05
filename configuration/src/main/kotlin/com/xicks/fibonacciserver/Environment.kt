package com.xicks.fibonacciserver

import com.typesafe.config.ConfigFactory

object Environment {
    val env = ConfigFactory.systemEnvironment()
    val hoconConfig = ConfigFactory.load().resolve()!!

    fun getOrDefault(fromEnv: String?, fromConfig: String?, default: String) : String {
        return listOf(env to fromEnv, hoconConfig to fromConfig)
            .asSequence()
            .mapNotNull { (config, property) ->
                if (property != null && config.hasPath(property)) {
                    config.getString(property)
                } else null
            }.firstOrNull() ?: default
    }
}