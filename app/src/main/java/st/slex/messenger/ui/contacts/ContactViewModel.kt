package st.slex.messenger.ui.contacts

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import st.slex.messenger.core.Resource
import st.slex.messenger.data.profile.UserDataMapper
import st.slex.messenger.data.profile.UserRepository
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
            .flatMapLatest {
                if (it is Resource.Success) Log.i("TAGNew", it.data.toString())
                flow {
                    val res = it.map(userMapper)
                    if (res is Resource.Success) Log.i("TAG_UI", res.data.toString())
                    emit(res)
                }
                //flowOf(it.map(userMapper))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = Resource.Loading
            )
}