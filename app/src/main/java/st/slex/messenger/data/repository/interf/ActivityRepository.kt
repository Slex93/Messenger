package st.slex.messenger.data.repository.interf

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import st.slex.messenger.data.model.ContactModel
import st.slex.messenger.utilites.result.Resource

interface ActivityRepository {
    suspend fun isAuthorise(): Flow<Resource<FirebaseAuth>>
    suspend fun initFirebase()
    suspend fun signOut()
    suspend fun statusOnline()
    suspend fun statusOffline()
    suspend fun updatePhonesToDatabase(listContact: List<ContactModel>)
}