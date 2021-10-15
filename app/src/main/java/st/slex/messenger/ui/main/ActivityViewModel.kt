package st.slex.messenger.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import st.slex.messenger.core.Resource
import st.slex.messenger.data.main_activity.ActivityRepository
import st.slex.messenger.ui.contacts.ContactsUI
import st.slex.messenger.ui.contacts.ContactsUIMapper
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ActivityViewModel @Inject constructor(
    private val repository: ActivityRepository,
    private val contactsManager: ContactsManager,
    private val mapper: ContactsUIMapper
) : ViewModel() {

    val contactsJob: Job by lazy {
        viewModelScope.launch(
            context = Dispatchers.IO,
            start = CoroutineStart.LAZY
        ) {
            getContacts().collect { updateContacts(it).collect() }
        }
    }

    private suspend fun getContacts(): Flow<List<ContactsUI>> =
        contactsManager.getContacts()

    private suspend fun updateContacts(list: List<ContactsUI>): StateFlow<Resource<Nothing?>> =
        repository.updateContacts(mapper.map(list))
            .onCompletion {
                contactsJob.cancelChildren()
                contactsJob.cancel()
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = Resource.Loading
            )

    fun changeState(state: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.changeState(state)
    }

}