package st.slex.messenger.data.repository.interf

import android.app.Activity
import kotlinx.coroutines.flow.Flow
import st.slex.messenger.utilites.result.AuthResult

interface AuthRepository {
    suspend fun signInWithPhone(phone: String, activity: Activity): Flow<AuthResult>
    suspend fun sendCode(id: String, code: String): Flow<AuthResult>
    suspend fun authUser()
}