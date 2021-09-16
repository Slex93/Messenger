package st.slex.messenger.domain.contacts

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import st.slex.messenger.data.contacts.ContactsDataMapper
import st.slex.messenger.data.contacts.ContactsRepository
import st.slex.messenger.domain.chats.ChatsDomainResult
import javax.inject.Inject

@ExperimentalCoroutinesApi
interface ContactsInteractor {
    fun getContacts(): Flow<ContactsDomainResult>
    class Base @Inject constructor(
        private val repository: ContactsRepository,
        private val mapper: ContactsDataMapper<ContactsDomainResult>
    ) : ContactsInteractor {

        override fun getContacts(): Flow<ContactsDomainResult> = callbackFlow {
            try {
                repository.getContacts().collect {
                    trySendBlocking(it.map(mapper))
                }
            } catch (exception: Exception) {
                trySendBlocking(ChatsDomainResult.Failure(exception))
            }
            awaitClose { }
        }
    }
}