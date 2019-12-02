package com.xicks.fibonacciserver.health


import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.xicks.fibonacciserver.HealthStatus
import com.xicks.fibonacciserver.ResourceHealth
import com.xicks.fibonacciserver.health.gateways.HealthGateway
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.RuntimeException

class HealthInteractorTest {

    private val healthGatewayOne: HealthGateway = mock()
    private val healthGatewayTwo: HealthGateway = mock()
    private lateinit var healthInteractor: HealthInteractor

    private val resourceNameOne = "test_1"
    private val resourceNameTwo = "test_2"

    @BeforeEach
    fun setup() {
        healthInteractor = HealthInteractor(listOf(healthGatewayOne, healthGatewayTwo))
        whenever(healthGatewayOne.resourceName).thenReturn(resourceNameOne)
        whenever(healthGatewayTwo.resourceName).thenReturn(resourceNameTwo)
    }

    @Test
    fun `should return UP when all gateways are healthy`() {
        runBlocking {
            whenever(healthGatewayOne.ping()).thenReturn(true)
            whenever(healthGatewayTwo.ping()).thenReturn(true)

            val healthCheck = healthInteractor.isHealthy()

            verify(healthGatewayOne).ping()
            verify(healthGatewayTwo).ping()

            val expected = listOf(
                ResourceHealth(resourceNameOne, HealthStatus.UP),
                ResourceHealth(resourceNameTwo, HealthStatus.UP)
            )
            Assertions.assertEquals(HealthStatus.UP, healthCheck.application)
            Assertions.assertEquals(expected, healthCheck.resources)
        }
    }

    @Test
    fun `should return DOWN when one gateway is not healthy`() {
        runBlocking {
            whenever(healthGatewayOne.ping()).thenReturn(true)
            whenever(healthGatewayTwo.ping()).thenReturn(false)

            val healthCheck = healthInteractor.isHealthy()

            verify(healthGatewayOne).ping()
            verify(healthGatewayTwo).ping()

            val expected = listOf(
                ResourceHealth(resourceNameOne, HealthStatus.UP),
                ResourceHealth(resourceNameTwo, HealthStatus.DOWN)
            )
            Assertions.assertEquals(HealthStatus.DOWN, healthCheck.application)
            Assertions.assertEquals(expected, healthCheck.resources)
        }
    }

    @Test
    fun `should return DOWN when one gateway throws exception`() {
        runBlocking {
            whenever(healthGatewayOne.ping()).thenReturn(true)
            whenever(healthGatewayTwo.ping()).thenThrow(RuntimeException())

            val healthCheck = healthInteractor.isHealthy()

            verify(healthGatewayOne).ping()
            verify(healthGatewayTwo).ping()

            val expected = listOf(
                ResourceHealth(resourceNameOne, HealthStatus.UP),
                ResourceHealth(resourceNameTwo, HealthStatus.DOWN)
            )
            Assertions.assertEquals(HealthStatus.DOWN, healthCheck.application)
            Assertions.assertEquals(expected, healthCheck.resources)
        }
    }

}