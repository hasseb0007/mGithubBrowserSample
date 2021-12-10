package com.invotyx.data.repository

import com.chibatching.kotpref.KotprefModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessTokenRepository @Inject constructor() {

    private object Pref : KotprefModel() {
        var accessTokenValue by nullableStringPref()
    }

    fun save(token: com.invotyx.example.model.AccessToken) {
        Pref.accessTokenValue = token.value
    }

    fun load(): com.invotyx.example.model.AccessToken? {
        return Pref.accessTokenValue?.let {
            com.invotyx.example.model.AccessToken(it)
        }
    }

    fun clear() {
        Pref.accessTokenValue = null
    }
}
