package st.slex.messenger.data.repository.interf

import st.slex.messenger.data.model.ContactModel

interface ActivityRepository {
    suspend fun changeState(state: String)
    suspend fun updateContacts(list: List<ContactModel>)
}