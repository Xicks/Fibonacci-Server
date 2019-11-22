package com.xicks.fibonacciserver.repository

interface CrudRepository<K,E> {
    suspend fun insert(entity: E): K
    suspend fun find(id: K): E?
    suspend fun findAll(): List<E>
}