package st.slex.messenger.ui.auth.engine.interf

import kotlinx.coroutines.flow.Flow
import st.slex.messenger.utilites.result.AuthResponse

interface SendCodeEngine {
    suspend fun sendCode(id: String, code: String): Flow<AuthResponse>
}