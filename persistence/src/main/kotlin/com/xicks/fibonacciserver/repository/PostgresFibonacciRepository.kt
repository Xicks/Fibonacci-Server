package com.xicks.fibonacciserver.repository

import com.xicks.fibonacciserver.Fibonacci
import org.postgresql.jdbc.PgConnection
import org.slf4j.LoggerFactory
import java.lang.IllegalStateException
import java.sql.PreparedStatement

class PostgresFibonacciRepository(private val connection: PgConnection) : CrudRepository<Int, Fibonacci> {

    private val log = LoggerFactory.getLogger(PostgresFibonacciRepository::class.java)

    init {
        log.info("Creating tables on Postgres")
        val sqlFile = PostgresFibonacciRepository::class.java.classLoader.getResourceAsStream("create-table.sql")
        if(sqlFile != null) {
            sqlFile.reader().forEachLine {
                connection.execSQLUpdate(it)
            }
        } else {
            log.warn("Sql initialization file not found!")
        }
    }

    override suspend fun findAll(): List<Fibonacci> {
        val select = connection.prepareStatement("SELECT * FROM fibonacci ORDER BY index;")
        val resultSet = select.executeQuery()

        return mutableListOf<Fibonacci>().apply {
            while(resultSet.next()) {
                add(Fibonacci(resultSet.getInt("index"), resultSet.getInt("value")))
            }
        }.toList()
    }

    override suspend fun find(id: Int): Fibonacci? {
        val select = connection.prepareStatement("SELECT * FROM fibonacci WHERE index = '$id';")
        val resultSet = select.executeQuery()
        return if(resultSet.next()) {
            Fibonacci(resultSet.getInt("index"), resultSet.getInt("value"))
        } else null
    }

    override suspend fun insert(entity: Fibonacci): Int {
        val found = find(entity.index)

        return if(found == null) {
            val insert = "INSERT INTO fibonacci(index, value) VALUES( ? , ? );"
            val create = connection.prepareStatement(insert)
            create.apply {
                setInt(1, entity.index)
                setInt(2, entity.value)
            }

            val result = create.executeUpdate()

            return if (result > 0) {
                entity.index
            } else {
                throw IllegalStateException("Could not insert fibonacci result!")
            }
        } else {
            found.index
        }
    }

}