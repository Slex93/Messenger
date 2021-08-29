package st.slex.messenger.data.repository.impl

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import st.slex.messenger.data.model.ContactModel
import st.slex.messenger.data.repository.interf.ContactsRepository
import st.slex.messenger.data.service.DatabaseSnapshot
import st.slex.messenger.utilites.Const.AUTH
import st.slex.messenger.utilites.Const.NODE_PHONE_CONTACT
import st.slex.messenger.utilites.Const.NODE_USER
import st.slex.messenger.utilites.Const.REF_DATABASE_ROOT
import st.slex.messenger.utilites.getThisValue
import st.slex.messenger.utilites.result.EventResponse
import st.slex.messenger.utilites.result.Resource
import javax.inject.Inject

@ExperimentalCoroutinesApi

class ContactsRepositoryImpl @Inject constructor(private val service: DatabaseSnapshot) :
    ContactsRepository {


    override suspend fun getContacts(): Flow<Resource<ContactModel>> = channelFlow {
        service.valueEventFlow(
            REF_DATABASE_ROOT.child(NODE_PHONE_CONTACT).child(AUTH.uid.toString())
        ).collect { it ->
            when (it) {
                is EventResponse.Success -> {
                    val listOfContacts =
                        it.snapshot.children.map { snapshot -> snapshot.getThisValue<ContactModel>() }
                    listOfContacts.forEach { contact ->
                        service.valueEventFlow(REF_DATABASE_ROOT.child(NODE_USER).child(contact.id))
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
                    trySendBlocking(Resource.Failure(it.databaseError.toException()))
                }
            }
        }
    }
}