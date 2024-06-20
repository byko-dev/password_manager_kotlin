package com.bykodev.passwordmanager.model

class PasswordPreviewModel() {

    constructor(_id: Int, _title: String, _username: String, _type: String, _url: String) : this() {
        id = _id
        title = _title
        username = _username
        type = _type
        url = _url
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
}