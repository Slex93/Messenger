package st.slex.messenger.ui.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import st.slex.messenger.core.Mapper
import st.slex.messenger.data.contacts.ContactsData
import st.slex.messenger.data.contacts.ContactsRepository
import st.slex.messenger.data.profile.UserData
import st.slex.messenger.data.profile.UserRepository
import st.slex.messenger.ui.core.UIResult
import st.slex.messenger.ui.user_profile.UserUI
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ContactViewModel
@Inject constructor(
    private val repository: ContactsRepository,
    private val mapper: Mapper.DataToUi<List<ContactsData>, UIResult<List<ContactsUI>>>,
    private val userRepository: UserRepository,
    private val userMapper: Mapper.DataToUi<UserData, UIResult<UserUI>>
) : ViewModel() {

    suspend fun getContacts(): StateFlow<UIResult<List<ContactsUI>>> =
        repository.getContacts().map { it.map(mapper) }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = UIResult.Loading
        )

    suspend fun getUser(uid: String): StateFlow<UIResult<UserUI>> =
        userRepository.getUser(uid).map { it.map(userMapper) }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = UIResult.Loading
        )
}