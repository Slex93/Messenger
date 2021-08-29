package st.slex.messenger.data.repository.interf

import kotlinx.coroutines.flow.Flow
import st.slex.messenger.utilites.result.EventResponse

interface SingleChatRepository {
    suspend fun getStatus(uid: String): Flow<EventResponse>
}