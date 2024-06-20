package com.bykodev.passwordmanager.core

import java.security.MessageDigest

class PasswordEncoder {
    companion object {
        fun hashPassword(password: String): String {
            val bytes = password.toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            return digest.fold("", { str, it -> str + "%02x".format(it) })
        }

        fun checkPassword(inputPassword: String, storedHash: String): Boolean {
            val hashedInput = hashPassword(inputPassword)
            return hashedInput == storedHash
        }
    }
}