package com.example.tastymood.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.otroexample.model.UserDetails
import kotlinx.coroutines.flow.map

const val USER_DATASTORE = "user_data"

val Context.preferenceDataStore : DataStore<Preferences> by
    preferencesDataStore(name = USER_DATASTORE)

class DataStoreManager(val context: Context) {
    companion object {
        val NAME = stringPreferencesKey("NAME")
        val DATE = stringPreferencesKey("DATE")
        val GENDER = stringPreferencesKey("GENDER")
    }

    suspend fun saveToDateStore(userDetail: UserDetails) {
        context.preferenceDataStore.edit {
            it[NAME] = userDetail.name
            it[DATE] = userDetail.date
            it[GENDER] = userDetail.gender
        }
    }

    fun getFromDataStore() = context.preferenceDataStore.data.map {
        UserDetails(
            name = it[NAME] ?: "",
            date = it[DATE] ?: "",
            gender = it[GENDER] ?: ""
        )
    }

    suspend fun clearDataStore() = context.preferenceDataStore.edit {
        it.clear()
    }
}