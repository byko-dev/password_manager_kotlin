package com.bykodev.passwordmanager.core

class StatusModel() {

    constructor(_message: String, _status: Boolean) : this() {
        message = _message
        status = _status
    }

    var message: String = ""
        get() = field
        set(value) {
            field = value
        }

    var status: Boolean = false
        get() = field
        set(value) {
            field = value
        }
}