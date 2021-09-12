package st.slex.messenger.domain.engine.interf

import kotlinx.coroutines.flow.Flow
import st.slex.messenger.utilites.result.AuthResponse

interface SendCodeEngine : AuthEngine {
    suspend fun sendCode(id: String, code: String): Flow<AuthResponse>
}