package com.bykodev.passwordmanager.helper

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WorkHistory
import androidx.compose.ui.graphics.vector.ImageVector
import com.bykodev.passwordmanager.R

class PasswordTypes(private val context: Context) {

    val work: String
        get() = context.getString(R.string.work_password_type)

    val school: String
        get() = context.getString(R.string.school_password_type)

    val socialMedia: String
        get() = context.getString(R.string.social_media_password_type)

    val banking: String
        get() = context.getString(R.string.banking_password_type)

    val entertainment: String
        get() = context.getString(R.string.entertainment_password_type)

    val email: String
        get() = context.getString(R.string.email_password_type)

    val home: String
        get() = context.getString(R.string.home_password_type)

    fun getAllStrings(): List<String> {
        return listOf(
            work,
            school,
            socialMedia,
            banking,
            entertainment,
            email,
            home
        )
    }

    fun getIconByPasswordType(type: String) : ImageVector
    {
        return when (type) {
            context.getString(R.string.work_password_type) -> Icons.Filled.WorkHistory
            context.getString(R.string.school_password_type) -> Icons.Filled.School
            context.getString(R.string.social_media_password_type) -> Icons.Filled.MenuBook
            context.getString(R.string.banking_password_type) -> Icons.Filled.MonetizationOn
            context.getString(R.string.entertainment_password_type) -> Icons.Filled.People
            context.getString(R.string.email_password_type) -> Icons.Filled.Email
            context.getString(R.string.home_password_type) -> Icons.Filled.Home
            else -> Icons.Default.Settings
        }
    }

}