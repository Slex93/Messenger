package st.slex.messenger.data.repository.interf

import kotlinx.coroutines.flow.Flow
import st.slex.messenger.core.Response
import st.slex.messenger.data.model.ContactModel

interface ContactsRepository {
    suspend fun getContacts(): Flow<Response<List<ContactModel>>>
}