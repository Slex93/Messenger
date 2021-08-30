package st.slex.messenger.data.repository.interf

import kotlinx.coroutines.flow.Flow
import st.slex.messenger.utilites.result.ChildEventResponse
import st.slex.messenger.utilites.result.ValueEventResponse

interface SingleChatRepository {
    suspend fun getStatus(uid: String): Flow<ValueEventResponse>
    suspend fun getMessages(limitToLast: Int): Flow<ChildEventResponse>
    suspend fun sendMessage(message: String, uid: String)
}