package com.bykodev.passwordmanager.model

import java.sql.Timestamp

class PasswordPreviewModel() {

    constructor(_id: Int, _title: String, _username: String, _type: String, _url: String, _description: String, _created_at: Timestamp) : this() {
        id = _id
        title = _title
        username = _username
        type = _type
        url = _url
        description = _description
        created_at = _created_at
    }

    var id: Int = 0
        get() = field
        set(value) {
            field = value
        }

    var title: String = ""
        get() = field
        set(value) {
            field = value
        }

    var username: String = ""
        get() = field
        set(value) {
            field = value
        }

    var type: String = ""
        get() = field
        set(value) {
            field = value
        }

    var url: String = ""
        get() = field
        set(value) {
            field = value
        }

    var description: String = ""
        get() = field
        set(value) {
            field = value
        }

    var created_at: Timestamp = Timestamp(System.currentTimeMillis())
        get() = field
        set(value) {
            field = value
        }
}