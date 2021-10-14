package st.slex.messenger.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import st.slex.messenger.domain.AuthInteractor
import st.slex.messenger.domain.LoginDomainMapper
import st.slex.messenger.domain.LoginDomainResult
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

@ExperimentalCoroutinesApi
class AuthViewModel @Inject constructor(
    private val interactor: AuthInteractor,
    private val mapper: LoginDomainMapper
) : ViewModel() {

    suspend fun login(phone: String): StateFlow<LoginUIResult> =
        interactor.login(phone).flatMapLatest {
            flowOf(map(it))
        }.stateIn(
            viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = LoginUIResult.Loading
        )

    suspend fun sendCode(id: String, code: String): Flow<LoginUIResult> =
        interactor.sendCode(id, code).flatMapLatest {
            flowOf(map(it))
        }.stateIn(
            viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = LoginUIResult.Loading
        )

    suspend fun map(result: LoginDomainResult): LoginUIResult =
        suspendCoroutine { continuation ->
            continuation.resumeWith(Result.success(mapper.map(result)))
        }
}