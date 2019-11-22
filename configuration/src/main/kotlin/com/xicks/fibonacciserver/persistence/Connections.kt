package com.xicks.fibonacciserver.persistence

import com.xicks.fibonacciserver.Env
import io.lettuce.core.ClientOptions
import io.lettuce.core.RedisClient
import io.lettuce.core.TimeoutOptions
import io.lettuce.core.api.async.RedisAsyncCommands
import org.postgresql.Driver
import org.postgresql.jdbc.PgConnection
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.Properties

object Connections {

    private val log = LoggerFactory.getLogger(this::class.java)

    val redis: RedisAsyncCommands<String, String>? = try {
        val host = Env.get("REDIS_HOST") ?: "localhost"
        val port = Env.get("REDIS_PORT") ?: "3349"
        val client = RedisClient.create("redis://$host:$port")

        client.options = ClientOptions.builder()
            .cancelCommandsOnReconnectFailure(true)
            .timeoutOptions(TimeoutOptions.enabled(Duration.ofSeconds(1)))
            .build()

        val connection = client.connect()
        connection.async()
    } catch (e: Exception) {
        log.error("Could not connect to redis", e)
        null
    }

    val postgres: PgConnection? = try {
        val user = Env.get("POSTGRES_USER") ?: "postgres"
        val database = Env.get("POSTGRES_DATABASE") ?: "postgres"
        val password = Env.get("POSTGRES_PASSWORD") ?: "admin"

        val host = Env.get("POSTGRES_HOST") ?: "localhost"
        val port = Env.getInt("POSTGRES_PORT") ?: 5432
        val properties = Properties().apply {
            setProperty("user", user)
            setProperty("password", password)
            setProperty("database", database)
        }
        Driver().connect("jdbc:postgresql://$host:$port/", properties) as PgConnection
    } catch (e: Exception) {
        log.error("Could not connect to postgres", e)
        null
    }

}