package com.bykodev.passwordmanager.database.repository

import com.bykodev.passwordmanager.database.models.Password
import java.sql.Connection

class PasswordRepositoryImpl(private val connection: Connection) : PasswordRepository {

    init {
        createTableIfNotExists()
    }

    override fun createTableIfNotExists() {
        connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS passwords (
                        id INTEGER PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        username VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        url VARCHAR(255),
                        type VARCHAR(50),
                        description TEXT,
                        ownerId INT,
                        created_at TIMESTAMP,
                        updated_at TIMESTAMP
                    )
                """).use { statement ->
            statement.execute()
        }
    }

    override fun getPasswordsByUserId(userId: Int): MutableList<Password> {
        val passwords = mutableListOf<Password>()

        connection.prepareStatement("SELECT * FROM passwords WHERE ownerId = ?").use { statement ->
            statement.setInt(1, userId)
            val resultSet = statement.executeQuery()
            while (resultSet.next()) {
                val password = Password(
                    _id = resultSet.getInt("id"),
                    _title = resultSet.getString("title"),
                    _username = resultSet.getString("username"),
                    _password = resultSet.getString("password"),
                    _url = resultSet.getString("url"),
                    _type = resultSet.getString("type"),
                    _description = resultSet.getString("description"),
                    _ownerId = resultSet.getInt("ownerId"),
                    _created_at = resultSet.getTimestamp("created_at"),
                    _updated_at = resultSet.getTimestamp("updated_at")
                )
                passwords.add(password)
            }
        }
        return passwords
    }

    override fun createPassword(password: Password) : Boolean {

        val sql = "INSERT INTO passwords (title, username, password, url, type, description, ownerId, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"

        connection.prepareStatement(sql).use { statement ->
            statement.setString(1, password.title)
            statement.setString(2, password.username)
            statement.setString(3, password.password)
            statement.setString(4, password.url)
            statement.setString(5, password.type)
            statement.setString(6, password.description)
            statement.setInt(7, password.ownerId)
            statement.setTimestamp(8, password.created_at)
            statement.setTimestamp(9, password.updated_at)

            val rowsAffected = statement.executeUpdate()
            return rowsAffected > 0
        }
    }

    override fun updatePassword(password: Password): Boolean {
        val sql = "UPDATE passwords SET title = ?, username = ?, password = ?, url = ?, type = ?, description = ?, updated_at = ? WHERE id = ?"

        connection.prepareStatement(sql).use { statement ->
            statement.setString(1, password.title)
            statement.setString(2, password.username)
            statement.setString(3, password.password)
            statement.setString(4, password.url)
            statement.setString(5, password.type)
            statement.setString(6, password.description)
            statement.setTimestamp(7, password.updated_at)
            statement.setInt(8, password.id)

            val rowsAffected = statement.executeUpdate()
            return rowsAffected > 0
        }
    }

    override fun getPasswordById(passwordId: Int): Password? {
        connection.prepareStatement("SELECT * FROM passwords WHERE id = ?").use { statement ->
            statement.setInt(1, passwordId)
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                return Password(
                    _id = resultSet.getInt("id"),
                    _title = resultSet.getString("title"),
                    _username = resultSet.getString("username"),
                    _password = resultSet.getString("password"),
                    _url = resultSet.getString("url"),
                    _type = resultSet.getString("type"),
                    _description = resultSet.getString("description"),
                    _ownerId = resultSet.getInt("ownerId"),
                    _created_at = resultSet.getTimestamp("created_at"),
                    _updated_at = resultSet.getTimestamp("updated_at")
                )
            }
        }
        return null
    }

    override fun deletePasswordById(id: Int) : Boolean {
        val sql = "DELETE FROM passwords WHERE id = ?"

        connection.prepareStatement(sql).use { statement ->
            statement.setInt(1, id)

            val rowsAffected = statement.executeUpdate()
            return rowsAffected > 0
        }
    }
}