package st.slex.messenger.data.repository.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import st.slex.messenger.data.model.ContactModel
import st.slex.messenger.data.model.UserModel
import st.slex.messenger.data.repository.interf.ContactsRepository
import st.slex.messenger.utilites.NODE_CONTACT
import st.slex.messenger.utilites.NODE_USER
import st.slex.messenger.utilites.base.AppValueEventListener
import st.slex.messenger.utilites.funs.getThisValue
import st.slex.messenger.utilites.result.Response
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ContactsRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference,
    private val auth: FirebaseAuth
) :
    ContactsRepository {
    override suspend fun getContacts(): Flow<Response<UserModel>> = callbackFlow {
        val referenceContact = databaseReference.child(NODE_CONTACT).child(auth.uid.toString())
        val listener = AppValueEventListener({ snapshot ->
            val list = snapshot.children.map {
                it.getThisValue<ContactModel>()
            }
            list.forEach { contact ->
                launch {
                    val reference = databaseReference.child(NODE_USER).child(contact.id)
                    val userListener = AppValueEventListener({ user ->
                        trySendBlocking(
                            Response.Success(
                                user.getThisValue<UserModel>()
                                    .copy(full_name = contact.full_name)
                            )
                        )
                    }, { exception ->
                        trySendBlocking(Response.Failure(exception))
                    })
                    reference.addValueEventListener(userListener)
                }
            }
        }, { exception ->
            trySendBlocking(Response.Failure(exception))
        })

        referenceContact.addValueEventListener(listener)
        awaitClose { referenceContact.removeEventListener(listener) }
    }
}