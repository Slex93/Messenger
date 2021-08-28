package st.slex.messenger.data.repository.impl

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import st.slex.messenger.data.model.Contact
import st.slex.messenger.data.repository.interf.ContactsRepository
import st.slex.messenger.utilites.Const
import st.slex.messenger.utilites.Const.AUTH
import st.slex.messenger.utilites.result.EventResponse
import st.slex.messenger.utilites.result.Resource
import st.slex.messenger.utilites.valueEventFlow
import javax.inject.Inject

@ExperimentalCoroutinesApi

class ContactsRepositoryImpl @Inject constructor() : ContactsRepository {

    override suspend fun getContacts(): Flow<Resource<List<Contact>>> = callbackFlow {
        Const.REF_DATABASE_ROOT
            .child(Const.NODE_PHONE_CONTACT)
            .child(AUTH.uid.toString()).valueEventFlow().collect {
                when (it) {
                    is EventResponse.Success -> {
                        val listOfPrimaryContacts = it.snapshot.children.map { snapshot ->
                            snapshot.getValue(Contact::class.java) ?: Contact()
                        }
                        val list = mutableListOf<Contact>()

                        listOfPrimaryContacts.forEach { itemContact ->
                            Const.REF_DATABASE_ROOT
                                .child(Const.NODE_USER)
                                .child(itemContact.id)
                                .valueEventFlow().collect { eventResponse ->
                                    when (eventResponse) {
                                        is EventResponse.Success -> {
                                            val contactPrimary =
                                                eventResponse.snapshot.getValue(Contact::class.java)
                                                    ?: Contact()
                                            val item =
                                                contactPrimary.copy(fullname = itemContact.fullname)
                                            list.add(item)
                                            trySendBlocking(Resource.Success(list))
                                        }
                                        is EventResponse.Cancelled -> {
                                            trySendBlocking(Resource.Failure(eventResponse.databaseError.toException()))
                                        }
                                    }


                                }
                        }
                    }
                    is EventResponse.Cancelled -> {
                        trySendBlocking(Resource.Failure(it.databaseError.toException()))
                    }
                }

            }
    }

}