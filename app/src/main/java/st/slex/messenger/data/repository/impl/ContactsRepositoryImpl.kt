package st.slex.messenger.data.repository.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import st.slex.messenger.data.model.UserModel
import st.slex.messenger.data.repository.interf.ContactsRepository
import st.slex.messenger.data.service.interf.DatabaseSnapshot
import st.slex.messenger.utilites.NODE_CONTACT
import st.slex.messenger.utilites.NODE_USER
import st.slex.messenger.utilites.funs.getThisValue
import st.slex.messenger.utilites.result.Response
import st.slex.messenger.utilites.result.ValueEventResponse
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ContactsRepositoryImpl @Inject constructor(
    private val service: DatabaseSnapshot,
    private val databaseReference: DatabaseReference,
    private val auth: FirebaseAuth
) :
    ContactsRepository {
    override suspend fun getContacts(): Flow<Response<UserModel>> = channelFlow {
        val event = service.valueEventFlow(
            databaseReference.child(NODE_CONTACT).child(auth.uid.toString())
        ).collect { contacts ->
            when (contacts) {
                is ValueEventResponse.Success -> {
                    val listOfContacts =
                        contacts.snapshot.children.map { snapshot -> snapshot.getThisValue<UserModel>() }
                    listOfContacts.forEach { contact ->
                        service.valueEventFlow(databaseReference.child(NODE_USER).child(contact.id))
                            .collect { response ->
                                when (response) {
                                    is ValueEventResponse.Success -> {
                                        trySendBlocking(Response.Success(response.snapshot.getThisValue<UserModel>()))
                                    }
                                    is ValueEventResponse.Cancelled -> {
                                        trySendBlocking(Response.Failure(response.databaseError.toException()))
                                    }
                                }
                            }
                    }
                }
                is ValueEventResponse.Cancelled -> {
                    trySendBlocking(Response.Failure(contacts.databaseError.toException()))
                }
            }
        }
        awaitClose { event }
    }
}