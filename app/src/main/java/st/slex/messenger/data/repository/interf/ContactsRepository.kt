package st.slex.messenger.data.repository.interf

import kotlinx.coroutines.flow.Flow
import st.slex.messenger.data.model.UserModel
import st.slex.messenger.utilites.result.Response

interface ContactsRepository {
    suspend fun getContacts(): Flow<Response<UserModel>>
}