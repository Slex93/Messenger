package st.slex.messenger.data.repository.impl

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import st.slex.messenger.data.model.ContactModel
import st.slex.messenger.data.repository.interf.ActivityRepository
import st.slex.messenger.utilites.*
import st.slex.messenger.utilites.base.AppValueEventListener
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ActivityRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference,
    private val auth: FirebaseUser,
) : ActivityRepository {

    override suspend fun changeState(state: String): Unit = withContext(Dispatchers.IO) {
        databaseReference.child(NODE_USER).child(auth.uid)
            .child(CHILD_STATE).setValue(state)
    }

    override suspend fun updateContacts(list: List<ContactModel>): Unit =
        withContext(Dispatchers.IO) {
            databaseReference.child(NODE_PHONE).addValueEventListener(
                AppValueEventListener { snapshotParent ->
                    snapshotParent.children.forEach { snapshot ->
                        list.forEach { contact ->
                            if (auth.uid != snapshot.key && snapshot.value == contact.phone) {
                                databaseReference.child(NODE_USER).child(snapshot.key.toString())
                                    .child(CHILD_URL)
                                    .addValueEventListener(AppValueEventListener { url ->
                                        val map = mapOf(
                                            CHILD_ID to snapshot.key.toString(),
                                            CHILD_FULL_NAME to contact.full_name,
                                            CHILD_PHONE to contact.phone,
                                            CHILD_URL to url.value.toString()
                                        )
                                        databaseReference.child(NODE_CONTACT)
                                            .child(auth.uid)
                                            .child(snapshot.key.toString())
                                            .updateChildren(map)

                                    })
                            }
                        }
                    }
                }
            )
        }
}