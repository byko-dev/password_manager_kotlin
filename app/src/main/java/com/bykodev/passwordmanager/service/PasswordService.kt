package com.bykodev.passwordmanager.service

import com.bykodev.passwordmanager.core.Aes256
import com.bykodev.passwordmanager.core.ApplicationContext
import com.bykodev.passwordmanager.core.StatusModel
import com.bykodev.passwordmanager.database.SQLDelightFactory
import com.bykodev.passwordmanager.database.models.Password
import com.bykodev.passwordmanager.database.repository.PasswordRepositoryImpl
import com.bykodev.passwordmanager.model.PasswordPreviewModel
import java.sql.Timestamp

class PasswordService {

    fun createPassword(password: Password) : StatusModel
    {
        try {
            validation(password)

            var mutablePassword = encryptPassword(password)
            mutablePassword.ownerId = ApplicationContext.getUserId()!!
            mutablePassword.created_at = Timestamp(System.currentTimeMillis())
            mutablePassword.updated_at = Timestamp(System.currentTimeMillis())

            val database = SQLDelightFactory().getDatabase()
                ?: throw Exception("Failed to establish a connection with the database.")

            val passwordRepositoryImpl = PasswordRepositoryImpl(database)
            passwordRepositoryImpl.createPassword(password)

            return StatusModel("Password added successfully.", true)
        }catch (exception: Exception) {
            return StatusModel(exception.message ?: "Oops! We encountered an unexpected issue.", false)
        }
    }

    fun update(password: Password) : StatusModel
    {
        try {
            validation(password)

            var mutablePassword = encryptPassword(password)
            mutablePassword.updated_at = Timestamp(System.currentTimeMillis())

            val database = SQLDelightFactory().getDatabase()
                ?: throw Exception("Failed to establish a connection with the database.")

            val passwordRepositoryImpl = PasswordRepositoryImpl(database)
            passwordRepositoryImpl.updatePassword(password);

            return StatusModel("Password added successfully.", true)
        }catch (exception: Exception) {
            return StatusModel(exception.message ?: "Oops! We encountered an unexpected issue.", false)
        }
    }

    fun getUserPasswords() : MutableList<PasswordPreviewModel>
    {
        val database = SQLDelightFactory().getDatabase()
            ?: throw Exception("Failed to establish a connection with the database.")

        val passwordRepositoryImpl = PasswordRepositoryImpl(database)
        val passwordList = passwordRepositoryImpl.getPasswordsByUserId(ApplicationContext.getUserId())

        val passwords = mutableListOf<PasswordPreviewModel>()
        Aes256.setDefaultPassword(ApplicationContext.getPassword())
        for (password in passwordList) {
            val passwordModel = PasswordPreviewModel(
                _id = password.id,
                _title = Aes256.decrypt(password.title),
                _username = Aes256.decrypt(password.username),
                _type = Aes256.decrypt(password.type),
                _url = Aes256.decrypt(password.url),
                _description = Aes256.decrypt(password.description)
            )

            passwords.add(passwordModel)
        }

        return passwords
    }

    fun getPasswordById(passwordId: Int) : Password
    {
        val database = SQLDelightFactory().getDatabase()
            ?: throw Exception("Failed to establish a connection with the database.")

        val passwordRepositoryImpl = PasswordRepositoryImpl(database)
        val password = passwordRepositoryImpl.getPasswordById(passwordId) ?:
            throw Exception("This password does not exist.")

        return decryptPassword(password)
    }

    fun delete(passwordId: Int)
    {
        val database = SQLDelightFactory().getDatabase()
            ?: throw Exception("Failed to establish a connection with the database.")

        val passwordRepositoryImpl = PasswordRepositoryImpl(database)
        passwordRepositoryImpl.deletePasswordById(passwordId)
    }


    private fun validation(password: Password)
    {
        if (password.title.isEmpty()){
            throw IllegalArgumentException("Title cannot be empty")
        }

        if (password.title.length < 3){
            throw IllegalArgumentException("Title must be at least 3 characters long")
        }

        if (password.username.isEmpty()){
            throw IllegalArgumentException("Username cannot be empty")
        }

        if (password.username.length < 3){
            throw IllegalArgumentException("Username must be at least 3 characters long")
        }

        if (password.password.isEmpty()){
            throw IllegalArgumentException("Password cannot be empty")
        }

        if (password.password.length < 3){
            throw IllegalArgumentException("Password must be at least 3 characters long")
        }

        if (password.type.isEmpty()){
            throw IllegalArgumentException("Type cannot be empty")
        }

        if (password.type.length < 3){
            throw IllegalArgumentException("Type must be at least 3 characters long")
        }
    }

    private fun encryptPassword(password: Password) : Password
    {
        Aes256.setDefaultPassword(ApplicationContext.getPassword())

        password.title = Aes256.encrypt(password.title)
        password.username = Aes256.encrypt(password.username)
        password.password = Aes256.encrypt(password.password)
        password.url = Aes256.encrypt(password.url)
        password.type = Aes256.encrypt(password.type)
        password.description = Aes256.encrypt(password.description)

        return password
    }

    private fun decryptPassword(password: Password) : Password
    {
        Aes256.setDefaultPassword(ApplicationContext.getPassword())

        password.title = Aes256.decrypt(password.title)
        password.username = Aes256.decrypt(password.username)
        password.password = Aes256.decrypt(password.password)
        password.url = Aes256.decrypt(password.url)
        password.type = Aes256.decrypt(password.type)
        password.description = Aes256.decrypt(password.description)

        return password
    }




}