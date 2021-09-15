package st.slex.messenger.data.repository.interf

import kotlinx.coroutines.flow.Flow
import st.slex.messenger.core.Response
import st.slex.messenger.data.model.MessageModel
import st.slex.messenger.data.model.UserModel

interface SingleChatRepository {
    suspend fun getUser(uid: String): Flow<Response<UserModel>>
    suspend fun getCurrentUser(uid: String): Flow<Response<UserModel>>
    suspend fun getMessages(uid: String, limitToLast: Int): Flow<Response<MessageModel>>
    suspend fun sendMessage(message: String, user: UserModel, currentUser: UserModel)
}