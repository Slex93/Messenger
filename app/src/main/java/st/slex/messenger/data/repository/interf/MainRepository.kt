package st.slex.messenger.data.repository.interf

import kotlinx.coroutines.flow.Flow
import st.slex.messenger.data.model.ChatListModel
import st.slex.messenger.utilites.result.Response

interface MainRepository {
    suspend fun getChatList(page: Int): Flow<Response<List<ChatListModel>>>
}