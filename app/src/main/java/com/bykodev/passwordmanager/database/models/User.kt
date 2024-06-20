package com.bykodev.passwordmanager.database.models

class User() {

    constructor(_id: Int, _username: String, _password: String) : this() {
        id = _id
        username = _username
        password = _password
    }

    constructor(_username: String, _password: String) : this() {
        username = _username
        password = _password
    }

    var id: Int = 0
        get() = field
        set(value) {
            field = value
        }

    var username: String = ""
        get() = field
        set(value) {
            field = value
        }

    var password: String = ""
        get() = field
        set(value) {
            field = value
        }
}
