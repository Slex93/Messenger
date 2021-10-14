package st.slex.messenger.ui.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import st.slex.messenger.core.Mapper
import st.slex.messenger.data.contacts.ContactsData
import st.slex.messenger.data.contacts.ContactsRepository
import st.slex.messenger.data.core.DataResult
import st.slex.messenger.ui.core.UIResult
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ContactViewModel
@Inject constructor(
    private val repository: ContactsRepository,
    private val mapper: Mapper.DataToUi<List<ContactsData>, UIResult<List<ContactsUI>>>
) : ViewModel() {

    suspend fun getContacts(): StateFlow<UIResult<List<ContactsUI>>> =
        repository.getContacts().mapIt().stateIn(
            viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = UIResult.Loading
        )

    private suspend fun Flow<DataResult<List<ContactsData>>>.mapIt(): Flow<UIResult<List<ContactsUI>>> =
        callbackFlow {
            try {
                this@mapIt.collect {
                    trySendBlocking(it.map(mapper))
                }
            } catch (exception: Exception) {
                trySendBlocking(UIResult.Failure(exception))
            }
            awaitClose { }
        }
}