package st.slex.messenger.ui.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import st.slex.messenger.core.TestResponse
import st.slex.messenger.domain.contacts.ContactsDomainMapper
import st.slex.messenger.domain.contacts.ContactsDomainResult
import st.slex.messenger.domain.contacts.ContactsInteractor
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ContactViewModel
@Inject constructor(
    private val interactor: ContactsInteractor,
    private val mapper: ContactsDomainMapper<ContactsUIResult>
) : ViewModel() {

    suspend fun getContacts(): StateFlow<ContactsUIResult> =
        interactor.getContacts().mapIt().stateIn(
            viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = ContactsUIResult.Loading
        )

    private suspend fun Flow<ContactsDomainResult>.mapIt(): Flow<ContactsUIResult> =
        callbackFlow {
            try {
                this@mapIt.collect {
                    trySendBlocking(it.map(mapper))
                }
            } catch (exception: Exception) {
                trySendBlocking(TestResponse.Failure(exception))
            }
            awaitClose { }
        }
}