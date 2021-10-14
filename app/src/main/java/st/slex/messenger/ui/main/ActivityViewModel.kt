package st.slex.messenger.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import st.slex.messenger.core.model.firebase.FirebaseContactModel
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

    suspend fun getContacts(): SharedFlow<List<FirebaseContactModel>> =
        contactsManager.setContacts().shareIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            replay = 0
        )

    suspend fun updateContacts(list: List<FirebaseContactModel>): StateFlow<UIResult<*>> =
        response.create(repository.updateContacts(list)).stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = UIResult.Loading
        )

    fun changeState(state: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.changeState(state)
    }

}