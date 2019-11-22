package com.xicks.fibonacciserver.health

import com.xicks.fibonacciserver.health.gateways.HealthGateway
import org.postgresql.jdbc.PgConnection

class PostgresHealthGatewayImpl (private val connection: PgConnection?) : HealthGateway {

    override val resourceName: String = "POSTGRES"

    override suspend fun ping(): Boolean = runCatching { connection!!.execSQLQuery("SELECT 1") }.isSuccess

}