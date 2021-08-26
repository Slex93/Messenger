package st.slex.messenger.data.repository.interf

import kotlinx.coroutines.flow.Flow
import st.slex.messenger.data.model.MessageModel
import st.slex.messenger.utilites.result.EventResponse

interface MainRepository {
    suspend fun getCurrentUser(): Flow<EventResponse>
    suspend fun getTestList(): Flow<List<MessageModel>>
}