package com.bykodev.passwordmanager.database


import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

class RealmDatabaseConnection {
    /*private var instance: Database? = null

    fun getDatabase(context: Context): Database {
        return instance ?: createDatabase(context).also { instance = it }
    }

    private fun createDatabase(context: Context): Database {
        val driver: SQLDroidDriver = SQLDroidDriver(context, "mydatabase.db")
        return Database(driver)
    }*/

}

fun initializeDatabase(): Connection? {
    try {
        // Załaduj sterownik SQLDroid

    } catch (e: Exception) {
        e.printStackTrace()
    }

    try {
        // Utwórz połączenie z bazą danych
        val connection = DriverManager.getConnection("jdbc:sqldroid:/data/data/com.bykodev.passwordmanager/databases/database.db")
        println("Connected to the database")

        // Utwórz tabelę, jeśli nie istnieje
        val stmt: Statement = connection.createStatement()
        val sql = """
            CREATE TABLE IF NOT EXISTS Users (
                ID INTEGER PRIMARY KEY AUTOINCREMENT,
                Name TEXT NOT NULL,
                Age INTEGER
            )
        """.trimIndent()
        stmt.executeUpdate(sql)
        stmt.close()

        // Dodaj użytkownika do bazy danych
        addUser(connection, "John Doe", 30)

        // Pobierz użytkowników
        getUsers(connection)

        // Zamknij połączenie
        //connection.close()

        return connection
    } catch (e: Exception) {
        println("Error connecting to database: ${e.message}")
        e.printStackTrace()
    }
    return null
}

fun addUser(connection: Connection, name: String, age: Int) {
    val sql = "INSERT INTO Users (Name, Age) VALUES (?, ?)"
    val pstmt = connection.prepareStatement(sql)
    pstmt.setString(1, name)
    pstmt.setInt(2, age)
    pstmt.executeUpdate()
    pstmt.close()
}

fun getUsers(connection: Connection) {

    Log.v("MyTag", "This is a verbose message")
    Log.d("MyTag", "This is a debug message")
    Log.i("MyTag", "This is an information message")
    Log.w("MyTag", "This is a warning message")
    Log.e("MyTag", "This is an error message")
    val sql = "SELECT ID, Name, Age FROM Users"
    val stmt = connection.createStatement()
    val rs: ResultSet = stmt.executeQuery(sql)
    while (rs.next()) {

        val id = rs.getInt("ID")
        val name = rs.getString("Name")
        val age = rs.getInt("Age")
        println("User: $id, $name, $age")
    }
    rs.close()
    stmt.close()
}

