package st.slex.messenger.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import st.slex.messenger.domain.LoginInteractor

@ExperimentalCoroutinesApi
class LoginViewModel(
    private val communication: LoginCommunication,
    private val interactor: LoginInteractor
) : ViewModel() {

    suspend fun login(engine: LoginEngine, phone: String): StateFlow<LoginUi> =
        handleResult {
            interactor.login(engine, phone)
        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            LoginUi.Initial
        )

    suspend fun sendCode(engine: SendCodeEngine, id: String, code: String): StateFlow<LoginUi> =
        handleResult {
            interactor.sendCode(engine, id, code)
        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            LoginUi.Initial
        )

    private fun handleResult(block: suspend () -> Flow<Auth>): Flow<LoginUi> = callbackFlow {
        communication.map(LoginUi.Progress())
        block().collect {
            val result = when (it) {
                is Auth.Fail -> {
                    (LoginUi.Failed(it.e.message ?: ""))
                }
                is Auth.SendCode -> {
                    (LoginUi.SendCode)
                }
                else -> {
                    (LoginUi.Success)
                }
            }
            communication.map(result).collect { loginUi ->
                trySendBlocking(loginUi)
            }
        }
        awaitClose { }
    }
}