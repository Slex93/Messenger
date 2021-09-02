package st.slex.messenger.data.repository.interf

import android.app.Activity
import kotlinx.coroutines.flow.Flow
import st.slex.messenger.utilites.result.AuthResponse
import st.slex.messenger.utilites.result.VoidResponse

interface AuthRepository {
    suspend fun signInWithPhone(activity: Activity, phone: String): Flow<AuthResponse>
    suspend fun sendCode(id: String, code: String): Flow<AuthResponse>
    suspend fun authUser(): Flow<VoidResponse>
}