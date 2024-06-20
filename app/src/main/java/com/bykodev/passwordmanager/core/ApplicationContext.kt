package com.bykodev.passwordmanager.core

object ApplicationContext {
    private var username: String? = null
    private var password: String? = null
    private var userId: Int? = null

    fun login(_username: String, _password: String, _userId: Int)
    {
        username = _username
        password = _password
        userId = _userId
    }

    fun logOut() {
        username = null
        password = null
        userId = null
    }

    fun getUsername(): String {
        return username ?: throw Exception("Your session has expired. Please log in again to continue.")
    }

    fun getPassword(): String {
        return password ?: throw Exception("Your session has expired. Please log in again to continue.")
    }

    fun getUserId(): Int {
        return userId ?: throw Exception("Your session has expired. Please log in again to continue.")
    }
}