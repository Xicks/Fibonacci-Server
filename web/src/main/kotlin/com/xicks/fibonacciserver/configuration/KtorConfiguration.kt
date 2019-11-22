package com.xicks.fibonacciserver.configuration

import com.typesafe.config.ConfigFactory
import com.xicks.fibonacciserver.HealthStatus
import com.xicks.fibonacciserver.health.HealthInteractor
import com.xicks.fibonacciserver.interactors.Interactors
import com.xicks.fibonacciserver.routes.fibonacciRoutes
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.config.HoconApplicationConfig
import io.ktor.features.AutoHeadResponse
import io.ktor.features.ContentNegotiation
import io.ktor.features.DataConversion
import io.ktor.gson.GsonConverter
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.DataConversionException
import io.ktor.util.KtorExperimentalAPI
import java.util.UUID
import java.util.concurrent.TimeUnit


object KtorConfiguration {

    @KtorExperimentalAPI
    fun startServer() {
        var managerServer: ApplicationEngine? = null

        try {
            managerServer = embeddedServer(Netty, applicationEngineEnvironment {

                val hoconConfig = ConfigFactory.load().resolve()

                config = HoconApplicationConfig(hoconConfig)
                val managementPort =
                    config.propertyOrNull("ktor.management.port")?.getString()?.toInt() ?: 8081

                connector {
                    host = "0.0.0.0"
                    port = managementPort
                }
                module {
                    management()
                }
            }).also {
                println("Starting management server at port 8081")
                it.start(false)
            }

            embeddedServer(Netty, applicationEngineEnvironment {

                val hoconConfig = ConfigFactory.load().resolve()

                config = HoconApplicationConfig(hoconConfig)
                val serverPort = config.propertyOrNull("ktor.deployment.port")?.getString()?.toInt() ?: 8080

                connector {
                    host = "0.0.0.0"
                    port = serverPort
                }
                module {
                    fibonacciServer()
                }
            }).also {
                println("Starting application server at port 8080")
                it.start(true)
            }
        } catch (e: Exception) {
            println(e)
        } finally {
            managerServer?.stop(0, 5, TimeUnit.SECONDS)
        }
    }

    private fun Application.management() {
        routing {
            get("/health") {
                val health = Interactors.healthInteractor.isHealthy()
                val statusCode = if(health.application == HealthStatus.UP) HttpStatusCode.OK else HttpStatusCode.InternalServerError
                call.respond(statusCode, health)
            }
        }

        install(AutoHeadResponse)

        install(ContentNegotiation) {
            register(ContentType.Application.Json, GsonConverter())
        }
    }

    private fun Application.fibonacciServer() {
        routing {
            fibonacciRoutes()
        }

        install(AutoHeadResponse)

        install(ContentNegotiation) {
            register(ContentType.Application.Json, GsonConverter())
        }

        install(DataConversion) {
            convert<UUID> {
                decode { values, _ ->
                    when(values.size) {
                        0 -> null
                        1 -> values.singleOrNull().let { UUID.fromString(it) }
                        else -> values.map { UUID.fromString(it) }
                    }
                }

                encode { value ->
                    when(value) {
                        null -> listOf()
                        is UUID -> listOf(value.toString())
                        else -> throw DataConversionException("Cannot convert $value as UUID")
                    }
                }
            }
        }
    }
}