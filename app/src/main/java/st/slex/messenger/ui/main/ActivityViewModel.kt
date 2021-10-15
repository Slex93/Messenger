package st.slex.messenger.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import st.slex.messenger.data.contacts.FirebaseContactModel
import st.slex.messenger.data.main_activity.ActivityRepository
import st.slex.messenger.ui.core.UIResult
import st.slex.messenger.ui.core.VoidUIResponse
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ActivityViewModel @Inject constructor(
    private val repository: ActivityRepository,
    private val response: VoidUIResponse,
    private val contactsManager: ContactsManager
) : ViewModel() {

    val contactsJob: Job = viewModelScope.launch(
        context = Dispatchers.IO,
        start = CoroutineStart.LAZY
    ) {
        getContacts().collect { updateContacts(it).collect() }
    }

    private suspend fun getContacts(): Flow<List<FirebaseContactModel>> =
        contactsManager.setContacts()

    private suspend fun updateContacts(list: List<FirebaseContactModel>): StateFlow<UIResult<*>> =
        response.create(repository.updateContacts(list)).onCompletion {
            contactsJob.cancelChildren()
            contactsJob.cancel()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = UIResult.Loading
        )

    fun changeState(state: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.changeState(state)
    }

}