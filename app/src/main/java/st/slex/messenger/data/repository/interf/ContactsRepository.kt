package st.slex.messenger.data.repository.interf

import kotlinx.coroutines.flow.Flow
import st.slex.messenger.data.model.UserModel
import st.slex.messenger.utilites.result.Resource

interface ContactsRepository {
    suspend fun getContacts(): Flow<Resource<UserModel>>
}