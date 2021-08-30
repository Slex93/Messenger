package st.slex.messenger.data.repository.interf

import kotlinx.coroutines.flow.Flow
import st.slex.messenger.data.model.MessageModel
import st.slex.messenger.utilites.result.Response

interface SingleChatRepository {
    suspend fun getStatus(uid: String): Flow<Response<String>>
    suspend fun getMessages(limitToLast: Int): Flow<Response<MessageModel>>
    suspend fun sendMessage(message: String, uid: String)
}