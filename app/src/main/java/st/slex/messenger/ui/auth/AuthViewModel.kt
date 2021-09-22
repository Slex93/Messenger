package st.slex.messenger.ui.auth

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import st.slex.messenger.domain.auth.AuthInteractor
import st.slex.messenger.domain.auth.LoginDomainResult
import st.slex.messenger.ui.core.VoidUIResponse
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AuthViewModel @Inject constructor(
    private val interactor: AuthInteractor
) : ViewModel() {

    suspend fun login(phone: String, activity: Activity): StateFlow<LoginUIResult> =
        interactor.login(phone, activity).mapLogin().stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(1000),
            initialValue = LoginUIResult.Loading
        )

    suspend fun sendCode(id: String, code: String): StateFlow<VoidUIResponse> =
        interactor.sendCode(id, code).mapSendCode().stateIn(
            viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = VoidUIResponse.Loading
        )

    private fun Flow<LoginDomainResult>.mapLogin(): Flow<LoginUIResult> = callbackFlow {
        this@mapLogin.collect {
            when (it) {
                is LoginDomainResult.Success -> trySendBlocking(LoginUIResult.Success)
                is LoginDomainResult.SendCode -> trySendBlocking(LoginUIResult.SendCode(it.id))
                is LoginDomainResult.Failure -> trySendBlocking(LoginUIResult.Failure(it.exception))
            }
        }
    }

    private fun Flow<LoginDomainResult>.mapSendCode(): Flow<VoidUIResponse> = callbackFlow {
        this@mapSendCode.collect {
            when (it) {
                is LoginDomainResult.Success -> trySendBlocking(VoidUIResponse.Success)
                is LoginDomainResult.Failure -> trySendBlocking(VoidUIResponse.Failure(it.exception))
                is LoginDomainResult.SendCode -> {
                }
            }
        }
    }

}