package com.bykodev.passwordmanager.service


import android.content.Context
import androidx.core.content.ContextCompat.getString
import com.bykodev.passwordmanager.R
import com.bykodev.passwordmanager.core.ApplicationContext
import com.bykodev.passwordmanager.core.PasswordEncoder
import com.bykodev.passwordmanager.database.SQLDelightFactory
import com.bykodev.passwordmanager.database.models.User
import com.bykodev.passwordmanager.database.repository.UserRepositoryImpl
import com.bykodev.passwordmanager.model.StatusModel

class UserService(private val context : Context) {

    fun createAccount(user: User, retypePassword: String) : StatusModel
    {
        try {
            validation(user, retypePassword)

            val database = SQLDelightFactory().getDatabase()
                ?: throw Exception( getString(context, R.string.exception_connecting_problem) )

            val userRepositoryImpl = UserRepositoryImpl(database)
            val existedUser = userRepositoryImpl.getUserByUsername(user.username)

            if (existedUser != null){
                throw Exception( getString(context, R.string.exception_user_already_exists) )
            }

            user.password = PasswordEncoder.hashPassword(user.password)
            userRepositoryImpl.addUser(user)

            return StatusModel( getString(context, R.string.success_user_created), true)
        }catch (exception: Exception){
            return StatusModel(exception.message ?: getString(context, R.string.exception_message), false)
        }
    }

    fun loginAttempt(user: User) : StatusModel
    {
        try {
            val database = SQLDelightFactory().getDatabase()
                ?: throw Exception( getString(context, R.string.exception_connecting_problem) )

            val userRepositoryImpl = UserRepositoryImpl(database)
            val existedUser = userRepositoryImpl.getUserByUsername(user.username)
                ?: throw Exception( getString(context, R.string.exception_user_does_not_exist))

            if (!PasswordEncoder.checkPassword(user.password, existedUser.password))
            {
                throw Exception( getString(context, R.string.login_failed) )
            }

            ApplicationContext.login(user.username, user.password, existedUser.id)

            return StatusModel(getString(context, R.string.login_success), true)
        }catch (exception: Exception) {
            return StatusModel(exception.message ?: getString(context, R.string.exception_message), false)
        }
    }

    private fun validation(user: User, retypePassword: String)
    {
        if (user.username.isEmpty()) {
            throw IllegalArgumentException( getString(context, R.string.validation_empty_username) )
        }
        if (user.username.length < 3) {
            throw IllegalArgumentException(getString(context, R.string.validation_too_short_username))
        }

        if (user.password.isEmpty()) {
            throw IllegalArgumentException( getString(context, R.string.validation_empty_password) )
        }
        if (user.password.length < 6) {
            throw IllegalArgumentException( getString(context, R.string.validation_too_short_password) )
        }

        if (user.password != retypePassword) {
            throw IllegalArgumentException(getString(context, R.string.validation_password_do_not_match))
        }
    }

}