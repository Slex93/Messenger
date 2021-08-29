package st.slex.messenger.data.repository.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import st.slex.messenger.data.model.ContactModel
import st.slex.messenger.data.repository.interf.ContactsRepository
import st.slex.messenger.data.service.DatabaseSnapshot
import st.slex.messenger.utilites.NODE_PHONE_CONTACT
import st.slex.messenger.utilites.NODE_USER
import st.slex.messenger.utilites.funs.getThisValue
import st.slex.messenger.utilites.result.EventResponse
import st.slex.messenger.utilites.result.Resource
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ContactsRepositoryImpl @Inject constructor(
    private val service: DatabaseSnapshot,
    private val databaseReference: DatabaseReference,
    private val auth: FirebaseAuth
) :
    ContactsRepository {
    override suspend fun getContacts(): Flow<Resource<ContactModel>> = channelFlow {
        val event = service.valueEventFlow(
            databaseReference.child(NODE_PHONE_CONTACT).child(auth.uid.toString())
        ).collect { contacts ->
            when (contacts) {
                is EventResponse.Success -> {
                    val listOfContacts =
                        contacts.snapshot.children.map { snapshot -> snapshot.getThisValue<ContactModel>() }
                    listOfContacts.forEach { contact ->
                        service.valueEventFlow(databaseReference.child(NODE_USER).child(contact.id))
                            .collect { response ->
                                when (response) {
                                    is EventResponse.Success -> {
                                        trySendBlocking(Resource.Success(response.snapshot.getThisValue<ContactModel>()))
                                    }
                                    is EventResponse.Cancelled -> {
                                        trySendBlocking(Resource.Failure(response.databaseError.toException()))
                                    }
                                }
                            }
                    }
                }
                is EventResponse.Cancelled -> {
                    trySendBlocking(Resource.Failure(contacts.databaseError.toException()))
                }
            }
        }
        awaitClose { event }
    }
}