package st.slex.messenger.data.repository.interf

import kotlinx.coroutines.flow.Flow
import st.slex.messenger.core.Response
import st.slex.messenger.data.model.ChatListModel

interface MainRepository {
    suspend fun getChatList(page: Int): Flow<Response<List<ChatListModel>>>
}