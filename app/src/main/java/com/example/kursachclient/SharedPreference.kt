package com.example.kursachclient

import android.content.Context
import android.content.SharedPreferences

private const val PREFS_NAME = "PREFS_NAME"

class SharedPreference(context: Context) {

    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveToken(value: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString("API_KEY", value)
        editor.apply()
    }

    fun getToken(): String {
        return sharedPref.getString("API_KEY", null) ?: ""
    }

    fun clearValue() {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }

    fun saveRole(value: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString("API_ROLE", value)
        editor.apply()
    }

    fun getRole(): String {
        return sharedPref.getString("API_ROLE", null) ?: ""
    }
}