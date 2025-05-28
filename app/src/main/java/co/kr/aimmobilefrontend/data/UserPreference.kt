package co.kr.aimmobilefrontend.data

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * data store 를 활용하기 위한 모듈
 */
class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val ID = stringPreferencesKey("id")
        val PWD = stringPreferencesKey("pwd")
        val EMAIL = stringPreferencesKey("email")
        val PHONE_NUMBER = stringPreferencesKey("phone_number")

    }

    /**
     * id 정보 flow 형태로 읽기.
     */
    val idFlow: Flow<String?> = dataStore.data.map { it[ID] }

    /**
     * 추후 필요시 활용
     */
//    val pwdFlow: Flow<String?> = dataStore.data.map { it[PWD] }
//    val emailFlow: Flow<String?> = dataStore.data.map { it[EMAIL] }
//    val phoneNumberFlow: Flow<String?> = dataStore.data.map { it[PHONE_NUMBER] }

    /**
     * 로그인 정보 저장
     */
    suspend fun saveUserInfo(
        id: String,
        pwd: String,
        email: String,
        phoneNumber: String
    ) {
        dataStore.edit { prefs ->
            prefs[ID] = id
            prefs[PWD] = pwd
            prefs[EMAIL] = email
            prefs[PHONE_NUMBER] = phoneNumber
        }
    }

    /**
     * 로그인 정보 삭제
     */
    suspend fun clear() {
        dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
