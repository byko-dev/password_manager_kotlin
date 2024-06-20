package com.bykodev.passwordmanager.database.repository

import com.bykodev.passwordmanager.database.models.Password

interface PasswordRepository {
    fun createTableIfNotExists()
    fun getPasswordsByUserId(userId: Int): MutableList<Password>
    fun createPassword(password: Password) : Boolean
    fun updatePassword(password: Password) : Boolean
    fun getPasswordById(passwordId: Int) : Password?
    fun deletePasswordById(id: Int) : Boolean
}