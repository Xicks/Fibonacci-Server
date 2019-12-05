package com.xicks.fibonacciserver.persistence

import com.xicks.fibonacciserver.Environment
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
        val host = Environment.getOrDefault("REDIS_HOST", "redis.host", "localhost")
        val port = Environment.getOrDefault("REDIS_PORT", "redis.port", "localhost")
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
        val user = Environment.getOrDefault("POSTGRES_USER", "postgres.user", "postgres")
        val database = Environment.getOrDefault("POSTGRES_DATABASE", "postgres.database", "postgres")
        val password = Environment.getOrDefault("POSTGRES_PASSWORD", "postgres.password", "admin")

        val host = Environment.getOrDefault("POSTGRES_HOST", "postgres.host", "localhost")
        val port = Environment.getOrDefault("POSTGRES_PORT", "postgres.port", "5432")
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