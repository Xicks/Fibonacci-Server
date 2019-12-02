package com.xicks.fibonacciserver.configuration

import com.typesafe.config.ConfigFactory
import com.xicks.fibonacciserver.HealthStatus
import com.xicks.fibonacciserver.calculateFibonacci.CalculateFibonacciInteractor
import com.xicks.fibonacciserver.calculateFibonacci.RetrieveFibonacciInteractor
import com.xicks.fibonacciserver.health.HealthInteractor
import com.xicks.fibonacciserver.routes.fibonacciRoutes
import io.ktor.application.Application
import io.ktor.application.ApplicationStarted
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


class KtorConfiguration(
    private val healthInteractor: HealthInteractor,
    private val calculationInteractor: CalculateFibonacciInteractor,
    private val retrieveFibonacciInteractor: RetrieveFibonacciInteractor
) {

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
            }).start(false)

            embeddedServer(Netty, applicationEngineEnvironment {

                val hoconConfig = ConfigFactory.load("reference.conf").resolve()

                config = HoconApplicationConfig(hoconConfig)
                val serverPort = config.propertyOrNull("ktor.deployment.port")?.getString()?.toInt() ?: 8080

                connector {
                    host = "0.0.0.0"
                    port = serverPort
                }
                module {
                    fibonacciServer(calculationInteractor, retrieveFibonacciInteractor)
                }
            }).start(true)
        } catch (e: Exception) {
            println(e)
        } finally {
            managerServer?.stop(0, 5, TimeUnit.SECONDS)
        }
    }

    @KtorExperimentalAPI
    private fun Application.management() {

        environment.monitor.subscribe(ApplicationStarted) {
            val port = environment.config.property("ktor.management.port").getString()
            println("Management services started at port $port...")
        }

        routing {
            get("/health") {
                val health = healthInteractor.isHealthy()
                val statusCode = if(health.application == HealthStatus.UP) HttpStatusCode.OK else HttpStatusCode.InternalServerError
                call.respond(statusCode, health)
            }
        }

        install(AutoHeadResponse)

        install(ContentNegotiation) {
            register(ContentType.Application.Json, GsonConverter())
        }
    }

    @KtorExperimentalAPI
    private fun Application.fibonacciServer(
        calculationInteractor: CalculateFibonacciInteractor,
        retrieveFibonacciInteractor: RetrieveFibonacciInteractor
    ) {

        environment.monitor.subscribe(ApplicationStarted) {
            val port = environment.config.property("ktor.deployment.port").getString()
            println("Application services started at port $port...")
        }

        routing {
            fibonacciRoutes(calculationInteractor, retrieveFibonacciInteractor)
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