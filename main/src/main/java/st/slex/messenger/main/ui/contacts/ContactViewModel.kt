package st.slex.messenger.main.ui.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import st.slex.core.Resource
import st.slex.messenger.main.data.contacts.ContactListDataMapper
import st.slex.messenger.main.data.contacts.ContactsRepository
import st.slex.messenger.main.data.user.UserDataMapper
import st.slex.messenger.main.data.user.UserRepository
import st.slex.messenger.main.ui.user_profile.UserUI
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ContactViewModel
@Inject constructor(
    private val userRepository: UserRepository,
    private val userMapper: UserDataMapper,
    private val contactRepository: ContactsRepository,
    private val contactMapper: ContactListDataMapper
) : ViewModel() {

    suspend fun getAllContacts(): StateFlow<Resource<List<ContactUI>>> =
        contactRepository.getContacts()
            .flatMapLatest { flowOf(it.map(contactMapper)) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = Resource.Loading
            )

    suspend fun getUser(uid: String): StateFlow<Resource<UserUI>> =
        userRepository.getUser(uid)
            .flatMapLatest { flowOf(it.map(userMapper)) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = Resource.Loading
            )
}