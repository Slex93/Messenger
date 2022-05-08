package st.slex.messenger.auth.domain.interf

import kotlinx.coroutines.flow.Flow
import st.slex.core.Resource

interface AuthRepository {
    suspend fun saveUser(): Flow<Resource<Void>>
}