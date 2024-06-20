package com.bykodev.passwordmanager.core

import android.content.Context
import android.content.SharedPreferences

class AppPreferencesManager(context: Context) {

    companion object
    {
        val KEY_REMEMBER_ME = "rememberMe"
        val KEY_USERNAME = "username"
        val KEY_PASSWORD = "password"
        val KEY_APP_MODE = "app_mode"
    }

    private val PREFERENCES_FILE_KEY = "com.bykodev.passwordmanager.core.app_state_prefs"
    private val preferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)

    fun saveState(key: String, value: String) {
        with (preferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getState(key: String): String? {
        return preferences.getString(key, null)
    }

    fun removeState(key: String) {
        with (preferences.edit()) {
            remove(key)
            apply()
        }
    }
}