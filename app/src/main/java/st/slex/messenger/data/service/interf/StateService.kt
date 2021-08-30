package st.slex.messenger.data.service.interf

import kotlinx.coroutines.flow.Flow
import st.slex.messenger.utilites.result.VoidResponse

interface StateService {
    suspend fun changeState(state: String): Flow<VoidResponse>
}