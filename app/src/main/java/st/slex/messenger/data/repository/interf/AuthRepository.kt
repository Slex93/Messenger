package st.slex.messenger.data.repository.interf

import android.app.Activity
import kotlinx.coroutines.flow.Flow
import st.slex.messenger.utilites.result.AuthResponse

interface AuthRepository {
    suspend fun signInWithPhone(phone: String, activity: Activity): Flow<AuthResponse>
    suspend fun sendCode(id: String, code: String): Flow<AuthResponse>
    suspend fun authUser()
}