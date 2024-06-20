package com.bykodev.passwordmanager.database.repository

import com.bykodev.passwordmanager.database.models.User


interface UserRepository {

    fun createTableIfNotExists()
    fun getUserById(userId: Int): User?
    fun addUser(user: User): Boolean
    fun getUserByUsername(username: String) : User?
}