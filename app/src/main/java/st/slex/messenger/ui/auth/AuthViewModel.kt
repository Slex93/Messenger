package st.slex.messenger.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import st.slex.messenger.domain.AuthInteractor
import st.slex.messenger.domain.LoginDomainResult
import st.slex.messenger.ui.core.VoidUIResult
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AuthViewModel @Inject constructor(
    private val interactor: AuthInteractor
) : ViewModel() {

    suspend fun login(phone: String): StateFlow<LoginUIResult> =
        interactor.login(phone).mapLogin().stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(1000),
            initialValue = LoginUIResult.Loading
        )

    suspend fun sendCode(id: String, code: String): StateFlow<VoidUIResult> =
        interactor.sendCode(id, code).mapSendCode().stateIn(
            viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = VoidUIResult.Loading
        )

    private fun Flow<LoginDomainResult>.mapLogin(): Flow<LoginUIResult> = callbackFlow {
        this@mapLogin.collect {
            it.mapLogin { result -> trySendBlocking(result) }
        }
        awaitClose { }
    }

    private fun Flow<LoginDomainResult>.mapSendCode(): Flow<VoidUIResult> = callbackFlow {
        this@mapSendCode.collect {
            it.mapSendCode { result -> trySendBlocking(result) }
        }
        awaitClose { }
    }

    private inline fun LoginDomainResult.mapLogin(crossinline function: (LoginUIResult) -> Unit) =
        when (this) {
            is LoginDomainResult.Success -> function(LoginUIResult.Success)
            is LoginDomainResult.SendCode -> function(LoginUIResult.SendCode(id))
            is LoginDomainResult.Failure -> function(LoginUIResult.Failure(exception))
        }

    private inline fun LoginDomainResult.mapSendCode(crossinline function: (VoidUIResult) -> Unit) =
        when (this) {
            is LoginDomainResult.Success -> function(VoidUIResult.Success)
            is LoginDomainResult.Failure -> function(VoidUIResult.Failure(exception))
            is LoginDomainResult.SendCode -> {
            }
        }
}