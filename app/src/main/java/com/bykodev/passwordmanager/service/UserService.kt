package com.bykodev.passwordmanager.service


import com.bykodev.passwordmanager.core.ApplicationContext
import com.bykodev.passwordmanager.core.PasswordEncoder
import com.bykodev.passwordmanager.core.StatusModel
import com.bykodev.passwordmanager.database.SQLDelightFactory
import com.bykodev.passwordmanager.database.models.User
import com.bykodev.passwordmanager.database.repository.UserRepositoryImpl

class UserService {

    fun createAccount(user: User, retypePassword: String) : StatusModel
    {
        try {
            validation(user, retypePassword)

            val database = SQLDelightFactory().getDatabase()
                ?: throw Exception("Failed to establish a connection with the database.")

            val userRepositoryImpl = UserRepositoryImpl(database)
            val existedUser = userRepositoryImpl.getUserByUsername(user.username)

            if (existedUser != null){
                throw Exception("This user already exists. If this is you, please log in instead.");
            }

            user.password = PasswordEncoder.hashPassword(user.password)
            userRepositoryImpl.addUser(user);

            return StatusModel("Account created successfully!", true)
        }catch (exception: Exception){
            return StatusModel(exception.message ?: "Oops! We encountered an unexpected issue.", false)
        }
    }

    fun loginAttempt(user: User) : StatusModel
    {
        try {
            val database = SQLDelightFactory().getDatabase()
                ?: throw Exception("Failed to establish a connection with the database.")

            val userRepositoryImpl = UserRepositoryImpl(database)
            val existedUser = userRepositoryImpl.getUserByUsername(user.username)
                ?: throw Exception("This user does not exist. Please sign up to get started.")

            if (!PasswordEncoder.checkPassword(user.password, existedUser.password))
            {
                throw Exception("Login failed, please verify your credentials and try again.")
            }

            ApplicationContext.login(user.username, user.password, existedUser.id)

            return StatusModel("Login completed successfully.", true)
        }catch (exception: Exception) {
            return StatusModel(exception.message ?: "Oops! We encountered an unexpected issue.", false)
        }
    }

    private fun validation(user: User, retypePassword: String)
    {
        if (user.username.isEmpty()) {
            throw IllegalArgumentException("Username cannot be empty")
        }
        if (user.username.length < 3) {
            throw IllegalArgumentException("Username must be at least 3 characters long")
        }

        if (user.password.isEmpty()) {
            throw IllegalArgumentException("Password cannot be empty")
        }
        if (user.password.length < 6) {
            throw IllegalArgumentException("Password must be at least 6 characters long")
        }

        if (user.password != retypePassword) {
            throw IllegalArgumentException("Passwords do not match")
        }
    }

}