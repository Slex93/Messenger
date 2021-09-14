package st.slex.messenger.ui.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import st.slex.messenger.data.model.ContactModel
import st.slex.messenger.data.repository.interf.ContactsRepository
import st.slex.messenger.utilites.result.Response
import javax.inject.Inject

class ContactViewModel @Inject constructor(private val repository: ContactsRepository) :
    ViewModel() {
    suspend fun getContact(): StateFlow<Response<List<ContactModel>>> =
        repository.getContacts().stateIn(
            viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = Response.Loading
        )
}