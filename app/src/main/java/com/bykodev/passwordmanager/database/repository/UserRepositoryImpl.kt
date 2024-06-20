package com.bykodev.passwordmanager.database.repository

import com.bykodev.passwordmanager.database.models.User
import java.sql.Connection

class UserRepositoryImpl(private val connection: Connection) : UserRepository {

    init {
        createTableIfNotExists()
    }

    override fun createTableIfNotExists() {
        connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS users (
                        id INTEGER PRIMARY KEY,
                        username VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL
                    )
                """).use { statement ->
            statement.execute()
        }
    }

    override fun getUserById(userId: Int): User? {
        connection.prepareStatement("SELECT * FROM users WHERE id = ?").use { statement ->
            statement.setInt(1, userId)
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                return User(
                    _id = resultSet.getInt("id"),
                    _username = resultSet.getString("username"),
                    _password = resultSet.getString("email")
                )
            }
        }
        return null
    }

    override fun addUser(user: User) : Boolean {
        connection.prepareStatement("INSERT INTO users (id, username, password) VALUES (?, ?, ?)").use { statement ->
            statement.setInt(1, user.id)
            statement.setString(2, user.username)
            statement.setString(3, user.password)
            statement.executeUpdate()
        }
        return false
    }

    override fun getUserByUsername(username: String): User? {
        connection.prepareStatement("SELECT * FROM users WHERE username = ?").use { statement ->
            statement.setString(1, username)
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                return User(
                    _id = resultSet.getInt("id"),
                    _username = resultSet.getString("username"),
                    _password = resultSet.getString("password")
                )
            }
        }
        return null
    }
}