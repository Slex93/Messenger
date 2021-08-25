package st.slex.messenger.data.repository.interf

import st.slex.messenger.data.model.ContactModel

interface ActivityRepository {
    suspend fun initFirebase()
    suspend fun signOut()
    suspend fun statusOnline()
    suspend fun statusOffline()
    suspend fun updatePhonesToDatabase(listContact: List<ContactModel>)
}