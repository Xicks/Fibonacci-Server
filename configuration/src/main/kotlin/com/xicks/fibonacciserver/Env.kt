package com.xicks.fibonacciserver

object Env {
    private val env = System.getenv()

    fun get(property: String) : String? = env[property]
    fun getInt(property: String) : Int? = env[property]?.toInt()

}