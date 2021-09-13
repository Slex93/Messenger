package st.slex.messenger.data.repository.interf

import kotlinx.coroutines.flow.Flow
import st.slex.messenger.data.model.ContactModel
import st.slex.messenger.utilites.result.VoidResponse

interface ActivityRepository {
    suspend fun changeState(state: String)
    suspend fun updateContacts(list: List<ContactModel>): Flow<VoidResponse>
}