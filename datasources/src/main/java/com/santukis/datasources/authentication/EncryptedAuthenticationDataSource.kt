package com.santukis.datasources.authentication

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.santukis.datasources.BuildConfig
import com.santukis.entities.authentication.Token

class EncryptedAuthenticationDataSource(context: Context) : AuthenticationDataSource {

    companion object {
        private const val AUTHENTICATION_PREFERENCES = "${BuildConfig.LIBRARY_PACKAGE_NAME}.authentication"
        private const val TOKEN_KEY = "Token"
        private const val EXPIRES_KEY = "Expires"
    }

    private val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    private val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

    private val preferences: SharedPreferences = EncryptedSharedPreferences.create(
        AUTHENTICATION_PREFERENCES,
        mainKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private var token: String
        set(value) = preferences.edit().putString(TOKEN_KEY, value).apply()
        get() {
            return preferences.getString(TOKEN_KEY, "") ?: ""
        }

    private var expires: Long
        set(value) = preferences.edit().putLong(EXPIRES_KEY, value).apply()
        get() {
            return preferences.getLong(EXPIRES_KEY, 0L)
        }

    override fun getToken(): Result<Token> =
        if (token.isNotEmpty() && System.currentTimeMillis() < expires) {
            Result.success(
                Token(
                    accessToken = token,
                    expires = expires
                )
            )

        } else {
            Result.failure(Exception("No token store"))
        }

    override fun saveToken(token: Token) {
        this.token = token.accessToken
        this.expires = System.currentTimeMillis() + (token.expires * 1000)
    }
}