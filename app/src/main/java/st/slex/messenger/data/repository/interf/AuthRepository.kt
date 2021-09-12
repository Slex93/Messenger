package st.slex.messenger.data.repository.interf

import kotlinx.coroutines.flow.Flow
import st.slex.messenger.utilites.result.VoidResponse

interface AuthRepository {
    suspend fun saveUser(): Flow<VoidResponse>
}