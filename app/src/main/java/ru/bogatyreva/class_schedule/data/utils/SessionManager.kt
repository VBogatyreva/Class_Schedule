package ru.bogatyreva.class_schedule.data.utils

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.google.android.datatransport.BuildConfig
import ru.bogatyreva.class_schedule.data.utils.Constants.ACCESS_TOKEN
import ru.bogatyreva.class_schedule.data.utils.Constants.APPLICATION_ID
import ru.bogatyreva.class_schedule.data.utils.Constants.REFRESH_TOKEN
import ru.bogatyreva.class_schedule.data.utils.Constants.USER_ID
import ru.bogatyreva.class_schedule.data.utils.Constants.USER_LOGIN
import ru.bogatyreva.class_schedule.data.utils.Constants.USER_PASSWORD
import javax.inject.Inject

class SessionManager @Inject constructor(
    private val prefs: SharedPreferences
) {
    @Synchronized
    fun getAccessToken(): String {

        val token = prefs.getString(ACCESS_TOKEN, "") ?: ""
        Log.d("SessionManager", "getAccessToken: $token")

        return token
    }

    @Synchronized
    fun getRefreshToken(): String = prefs.getString(REFRESH_TOKEN, "") ?: ""

    @Synchronized
    fun getDeviceId(): String = prefs.getString(APPLICATION_ID, "") ?: ""

    @Synchronized
    fun getUserId(): String = prefs.getString(USER_ID, "") ?: ""

    @Synchronized
    fun getUserEmail(): String = prefs.getString(USER_LOGIN, "") ?: ""

    @Synchronized
    fun getUserPassword(): String = prefs.getString(USER_PASSWORD, "") ?: ""

    @Synchronized
    fun saveUserId(userId: String) {
        prefs.edit { putString(USER_ID, userId) }
    }

    @Synchronized
    fun saveDeviceId(appId: String) {
        Log.d("SessionManager", "saveApplicationId: $appId")

        prefs.edit { putString(APPLICATION_ID, appId) }
    }

    @Synchronized
    fun saveCredentials(login: String, password: String) {
        prefs.edit {
            putString(USER_LOGIN, login)
            putString(USER_PASSWORD, password)
        }
    }

    @Synchronized
    fun clearTokens() {
        val appId = prefs.getString(APPLICATION_ID, "") ?: ""
        prefs.edit {
            clear()
            putString(APPLICATION_ID, appId)
        }
    }

    @Synchronized
    fun updateAccessAndRefreshToken(userTokenDTO: String) {
        prefs.edit {
            putString(ACCESS_TOKEN, userTokenDTO)
            putString(REFRESH_TOKEN, userTokenDTO)
        }

        if (BuildConfig.DEBUG) Log.d("SessionManager", "updateAccessAndRefreshToken: $userTokenDTO")

    }
}