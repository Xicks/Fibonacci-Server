package com.xicks.fibonacciserver.repository

class StubbedCrudRepository<I,E>(
    private val insertFunction: () -> I,
    private val findFunction: (I) -> E? = { null },
    private val findAllFunction: () -> List<E> = { emptyList()}
) : CrudRepository<I,E> {

    override suspend fun insert(entity: E): I = insertFunction()

    override suspend fun find(id: I): E? = findFunction(id)

    override suspend fun findAll(): List<E> = findAllFunction()

}

class StubbedCacheRepository<I,E>(
    private val putFunction: () -> Unit = {},
    private val getFunction: (I) -> E? = { null }
) : CacheRepository<I,E> {

    override suspend fun put(key: I, value: E) = putFunction()

    override suspend fun get(key: I): E? = getFunction(key)

}