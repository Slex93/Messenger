package st.slex.messenger.data.repository.interf

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import st.slex.messenger.data.model.ChatListModel
import st.slex.messenger.data.model.UserModel
import st.slex.messenger.utilites.result.Response

interface MainRepository {
    val list: LiveData<Response<ChatListModel>>
    suspend fun getCurrentUser(): Flow<Response<UserModel>>
    suspend fun getChatList()
}