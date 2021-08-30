package st.slex.messenger.data.service.interf

import kotlinx.coroutines.flow.Flow
import st.slex.messenger.utilites.result.VoidResponse

interface StateService {
    suspend fun stateOnline(): Flow<VoidResponse>
    suspend fun stateOffline(): Flow<VoidResponse>
}