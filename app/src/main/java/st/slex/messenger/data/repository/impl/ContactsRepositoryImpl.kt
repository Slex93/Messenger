package st.slex.messenger.data.repository.impl

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.core.AppValueEventListener
import st.slex.messenger.data.model.ContactModel
import st.slex.messenger.data.model.ContactModelRemote
import st.slex.messenger.data.model.UserModel
import st.slex.messenger.data.repository.interf.ContactsRepository
import st.slex.messenger.utilites.NODE_CONTACT
import st.slex.messenger.utilites.NODE_USER
import st.slex.messenger.utilites.funs.getThisValue
import st.slex.messenger.utilites.result.Response
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ContactsRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference,
    private val user: FirebaseUser
) : ContactsRepository {
    override suspend fun getContacts(): Flow<Response<List<ContactModel>>> = callbackFlow {
        val reference = databaseReference
            .child(NODE_CONTACT)
            .child(user.uid)
        val listener = AppValueEventListener({ snapshot ->
            val contactList: List<ContactModel> = snapshot.children.map {
                it.getThisValue()
            }
            trySendBlocking(Response.Success(contactList))
        }, {
            trySendBlocking(Response.Failure(it))
        })
        reference.addValueEventListener(listener)
        awaitClose { reference.removeEventListener(listener) }
    }

    private suspend fun getUser(contact: ContactModelRemote) = callbackFlow<ContactModel> {
        val reference = databaseReference
            .child(NODE_USER)
            .child(contact.id)
        val listener = AppValueEventListener({
            val userRemote = it.getThisValue<UserModel>()
            val fullName = if (contact.full_name.isEmpty()) {
                if (userRemote.full_name.isEmpty()) {
                    userRemote.username
                } else {
                    userRemote.full_name
                }
            } else contact.full_name

            val result = ContactModel(
                contact.id,
                contact.phone,
                fullName,
                userRemote.url
            )
            trySendBlocking(result)
        }, { _ ->
        })
        reference.addValueEventListener(listener)
        awaitClose { reference.removeEventListener(listener) }
    }
}