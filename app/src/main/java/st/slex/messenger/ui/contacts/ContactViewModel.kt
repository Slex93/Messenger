package st.slex.messenger.ui.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import st.slex.messenger.core.Resource
import st.slex.messenger.data.user.UserDataMapper
import st.slex.messenger.data.user.UserRepository
import st.slex.messenger.ui.user_profile.UserUI
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ContactViewModel
@Inject constructor(
    private val userRepository: UserRepository,
    private val userMapper: UserDataMapper
) : ViewModel() {

    suspend fun getUser(uid: String): StateFlow<Resource<UserUI>> =
        userRepository.getUser(uid)
            .flatMapLatest { flowOf(it.map(userMapper)) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = Resource.Loading
            )
}