package com.bykodev.passwordmanager.service

import android.content.Context
import androidx.core.content.ContextCompat.getString
import com.bykodev.passwordmanager.R
import com.bykodev.passwordmanager.core.Aes256
import com.bykodev.passwordmanager.core.ApplicationContext
import com.bykodev.passwordmanager.core.StatusModel
import com.bykodev.passwordmanager.database.SQLDelightFactory
import com.bykodev.passwordmanager.database.models.Password
import com.bykodev.passwordmanager.database.repository.PasswordRepositoryImpl
import com.bykodev.passwordmanager.model.PasswordPreviewModel
import java.sql.Timestamp

class PasswordService(private val context : Context ) {

    fun createPassword(password: Password) : StatusModel
    {
        try {
            validation(password)

            var mutablePassword = encryptPassword(password)
            mutablePassword.ownerId = ApplicationContext.getUserId()!!
            mutablePassword.created_at = Timestamp(System.currentTimeMillis())
            mutablePassword.updated_at = Timestamp(System.currentTimeMillis())

            val database = SQLDelightFactory().getDatabase()
                ?: throw Exception( getString( context, R.string.exception_connecting_problem ) )

            val passwordRepositoryImpl = PasswordRepositoryImpl(database)
            passwordRepositoryImpl.createPassword(password)

            return StatusModel( getString( context, R.string.success_password_created ), true)
        }catch (exception: Exception) {
            return StatusModel(exception.message ?: getString( context, R.string.exception_message ), false)
        }
    }

    fun update(password: Password) : StatusModel
    {
        try {
            validation(password)

            var mutablePassword = encryptPassword(password)
            mutablePassword.updated_at = Timestamp(System.currentTimeMillis())

            val database = SQLDelightFactory().getDatabase()
                ?: throw Exception( getString( context, R.string.exception_connecting_problem ) )

            val passwordRepositoryImpl = PasswordRepositoryImpl(database)
            passwordRepositoryImpl.updatePassword(password);

            return StatusModel( getString( context, R.string.success_password_updated ), true)
        }catch (exception: Exception) {
            return StatusModel(exception.message ?: getString( context, R.string.exception_message ), false)
        }
    }

    fun getUserPasswords() : MutableList<PasswordPreviewModel>
    {
        val database = SQLDelightFactory().getDatabase()
            ?: throw Exception( getString( context, R.string.exception_connecting_problem ) )

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
                _description = Aes256.decrypt(password.description),
                _created_at = password.created_at
            )

            passwords.add(passwordModel)
        }

        return passwords
    }

    fun getPasswordById(passwordId: Int) : Password
    {
        val database = SQLDelightFactory().getDatabase()
            ?: throw Exception( getString( context, R.string.exception_connecting_problem ) )

        val passwordRepositoryImpl = PasswordRepositoryImpl(database)
        val password = passwordRepositoryImpl.getPasswordById(passwordId) ?:
            throw Exception( getString( context, R.string.exception_empty_password ) )

        return decryptPassword(password)
    }

    fun delete(passwordId: Int)
    {
        val database = SQLDelightFactory().getDatabase()
            ?: throw Exception( getString( context, R.string.exception_connecting_problem ) )

        val passwordRepositoryImpl = PasswordRepositoryImpl(database)
        passwordRepositoryImpl.deletePasswordById(passwordId)
    }


    private fun validation(password: Password)
    {
        if (password.title.isEmpty()){
            throw IllegalArgumentException( getString(context, R.string.validation_empty_title) )
        }

        if (password.title.length < 3){
            throw IllegalArgumentException( getString(context, R.string.validation_too_short_title) )
        }

        if (password.username.isEmpty()){
            throw IllegalArgumentException( getString(context, R.string.validation_empty_username) )
        }

        if (password.username.length < 3){
            throw IllegalArgumentException( getString(context, R.string.validation_too_short_username) )
        }

        if (password.password.isEmpty()){
            throw IllegalArgumentException( getString(context, R.string.validation_empty_password) )
        }

        if (password.password.length < 3){
            throw IllegalArgumentException( getString(context, R.string.validation_too_short_password) )
        }

        if (password.type.isEmpty()){
            throw IllegalArgumentException( getString(context, R.string.validation_empty_type) )
        }

        if (password.type.length < 3){
            throw IllegalArgumentException( getString(context, R.string.validation_too_short_type) )
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