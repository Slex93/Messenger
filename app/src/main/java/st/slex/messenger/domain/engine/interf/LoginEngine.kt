package st.slex.messenger.domain.engine.interf

import android.app.Activity
import kotlinx.coroutines.flow.Flow
import st.slex.messenger.utilites.result.AuthResponse

interface LoginEngine : AuthEngine {
    suspend fun login(phone: String, activity: Activity): Flow<AuthResponse>
}