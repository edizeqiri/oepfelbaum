package com.edizeqiri.repository


abstract class LoanJsonRepository<T> {

    abstract var data: List<T>

    abstract var filePath: String

    fun load(): String {
        val json = object {}.javaClass.getResource(filePath)
            ?: throw IllegalArgumentException("The file does not exist and can not be loaded as data for the repository")

        return json.readText()
    }
}