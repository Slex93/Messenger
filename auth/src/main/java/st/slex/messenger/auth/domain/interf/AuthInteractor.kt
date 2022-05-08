package st.slex.messenger.auth.domain.interf

import kotlinx.coroutines.flow.Flow
import st.slex.messenger.auth.core.LoginValue

interface AuthInteractor {
    suspend fun login(phone: String): Flow<LoginValue>
    suspend fun sendCode(id: String, code: String): Flow<LoginValue>
}