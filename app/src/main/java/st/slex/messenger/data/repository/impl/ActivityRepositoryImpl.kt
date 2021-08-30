package st.slex.messenger.data.repository.impl

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import st.slex.messenger.data.model.ContactModel
import st.slex.messenger.data.repository.interf.ActivityRepository
import st.slex.messenger.data.service.interf.DatabaseSnapshot
import st.slex.messenger.data.service.interf.StateService
import st.slex.messenger.utilites.*
import st.slex.messenger.utilites.result.ValueEventResponse
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ActivityRepositoryImpl @Inject constructor(
    private val service: DatabaseSnapshot,
    private val databaseReference: DatabaseReference,
    private val auth: FirebaseAuth,
    private val stateService: StateService
) : ActivityRepository {

    override suspend fun changeState(state: String): Unit = withContext(Dispatchers.IO) {
        stateService.changeState(state).collect()
    }

    override suspend fun updateContacts(list: List<ContactModel>) = withContext(Dispatchers.IO) {
        service.singleValueEventFlow(databaseReference.child(NODE_PHONE)).collect {
            when (it) {
                is ValueEventResponse.Success -> {
                    it.snapshot.children.forEach { snapshot ->
                        list.forEach { contact ->
                            if (auth.currentUser?.uid.toString() != snapshot.key && snapshot.value == contact.phone) {
                                val map = mapOf(
                                    CHILD_ID to snapshot.key.toString(),
                                    CHILD_FULL_NAME to contact.full_name,
                                    CHILD_PHONE to contact.phone
                                )
                                databaseReference.child(NODE_CONTACT)
                                    .child(auth.currentUser?.uid.toString())
                                    .child(snapshot.value.toString())
                                    .updateChildren(map)
                            }
                        }
                    }
                }
                is ValueEventResponse.Cancelled -> {
                    Log.e("$this", it.databaseError.toString())
                }
            }
        }
    }
}