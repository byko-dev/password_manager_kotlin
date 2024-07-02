package com.bykodev.passwordmanager.database

import java.sql.Connection
import java.sql.DriverManager

class SQLDelightFactory {

    private var instance: Connection? = null

    fun getDatabase(): Connection? {
        return instance ?: createDatabase().also { instance = it }
    }

    private fun createDatabase(): Connection? {
        try {
            Class.forName("org.sqldroid.SQLDroidDriver")

            val connection = DriverManager.getConnection("jdbc:sqldroid:/data/data/com.bykodev.passwordmanager/databases/database.db")
            println("Connected to the database")
            return connection
        }
        catch (e: Exception)
        {
            println("Error connecting to database: ${e.message}")
            e.printStackTrace()
            return null
        }
    }
}