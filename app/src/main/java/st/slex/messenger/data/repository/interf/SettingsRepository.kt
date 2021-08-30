package st.slex.messenger.data.repository.interf

import kotlinx.coroutines.flow.Flow
import st.slex.messenger.utilites.result.VoidResponse

interface SettingsRepository {
    suspend fun signOut(state: String): Flow<VoidResponse>
}