package st.slex.messenger.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import st.slex.messenger.auth.domain.AuthInteractor
import st.slex.messenger.auth.domain.LoginDomainMapper
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AuthViewModel @Inject constructor(
    private val interactor: AuthInteractor,
    private val mapper: LoginDomainMapper
) : ViewModel() {

    suspend fun login(phone: String): StateFlow<LoginUIResult> =
        interactor.login(phone)
            .flatMapLatest { flowOf(mapper.map(it)) }
            .stateIn(
                viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = LoginUIResult.Loading
            )

    suspend fun sendCode(id: String, code: String): Flow<LoginUIResult> =
        interactor.sendCode(id, code)
            .flatMapLatest { flowOf(mapper.map(it)) }
            .stateIn(
                viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = LoginUIResult.Loading
            )
}