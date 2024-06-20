package com.bykodev.passwordmanager.database.models

import java.sql.Timestamp

class Password() {

    constructor(_id: Int, _title: String, _username: String, _password: String, _url: String,
                _type: String, _description: String, _ownerId: Int, _created_at: Timestamp, _updated_at: Timestamp) : this() {
        id = _id
        title = _title
        username = _username
        password = _password
        url = _url
        type = _type
        description = _description
        ownerId = _ownerId
        created_at = _created_at
        updated_at = _updated_at
    }

    constructor(_title: String, _username: String, _password: String, _url: String, _type: String, _description: String) : this()
    {
        title = _title
        username = _username
        password = _password
        url = _url
        type = _type
        description = _description
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

    var password: String = ""
        get() = field
        set(value) {
            field = value
        }

    var url: String = ""
        get() = field
        set(value) {
            field = value
        }

    var type: String = ""
        get() = field
        set(value) {
            field = value
        }

    var description: String = ""
        get() = field
        set(value) {
            field = value
        }

    var ownerId: Int = 0
        get() = field
        set(value) {
            field = value
        }

    var created_at: Timestamp = Timestamp(System.currentTimeMillis())
        get() = field
        set(value) {
            field = value
        }

    var updated_at: Timestamp = Timestamp(System.currentTimeMillis())
        get() = field
        set(value) {
            field = value
        }
}