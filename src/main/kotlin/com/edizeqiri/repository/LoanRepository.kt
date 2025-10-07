package com.edizeqiri.repository

abstract class LoanRepository<T> {

    abstract var data: List<T>

    abstract var file: String

    fun load(): String {
        val json = object {}.javaClass.getResource(file)
            ?: throw IllegalArgumentException("The file does not exist and can not be loaded as data for the repository")

        return json.readText()
    }
}