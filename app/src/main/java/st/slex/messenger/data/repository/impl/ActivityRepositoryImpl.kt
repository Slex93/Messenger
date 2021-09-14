package st.slex.messenger.data.repository.impl

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import st.slex.messenger.core.AppValueEventListener
import st.slex.messenger.data.model.ContactModel
import st.slex.messenger.data.repository.interf.ActivityRepository
import st.slex.messenger.utilites.*
import st.slex.messenger.utilites.result.VoidResponse
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ActivityRepositoryImpl @Inject constructor(
    private val reference: DatabaseReference,
    private val auth: FirebaseUser,
) : ActivityRepository {

    override suspend fun changeState(state: String): Unit = withContext(Dispatchers.IO) {
        reference.child(NODE_USER).child(auth.uid)
            .child(CHILD_STATE).setValue(state)
    }

    override suspend fun updateContacts(list: List<ContactModel>): Flow<VoidResponse> =
        callbackFlow {
            val phonesReference = reference.child(NODE_PHONE)
            val listener = AppValueEventListener({ snapshotListPhone ->
                snapshotListPhone.children.forEach { snapshotPhone ->
                    if (list.isNullOrEmpty()) {
                        reference.child(NODE_CONTACT).child(auth.uid).setValue(list)
                    } else {
                        list.forEach { contact ->
                            if (auth.uid != snapshotPhone.key && contact.phone == snapshotPhone.value) {
                                reference
                                    .child(NODE_USER)
                                    .child(snapshotPhone.key.toString())
                                    .child(CHILD_URL)
                                    .addValueEventListener(
                                        AppValueEventListener({ snapshotUrl ->
                                            val url = snapshotUrl.value
                                            val map = mapOf(
                                                CHILD_ID to snapshotPhone.key.toString(),
                                                CHILD_PHONE to contact.phone,
                                                CHILD_FULL_NAME to contact.full_name,
                                                CHILD_URL to url
                                            )
                                            val contactTask = reference
                                                .child(NODE_CONTACT)
                                                .child(auth.uid)
                                                .child(snapshotPhone.key.toString())
                                                .setValue(map)
                                            contactTask.addOnCompleteListener {
                                                if (it.isSuccessful) {
                                                    trySendBlocking(VoidResponse.Success)
                                                } else {
                                                    trySendBlocking(VoidResponse.Failure(it.exception!!))
                                                }
                                            }
                                        }, {
                                            trySendBlocking(VoidResponse.Failure(it))
                                        })
                                    )
                            }
                        }
                    }
                }
            }, {
                trySendBlocking(VoidResponse.Failure(it))
            })

            phonesReference.addValueEventListener(listener)
            awaitClose { phonesReference.removeEventListener(listener) }
        }

}