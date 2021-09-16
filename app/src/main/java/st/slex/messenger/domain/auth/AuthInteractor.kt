package st.slex.messenger.domain.auth

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import st.slex.messenger.data.auth.AuthRepository
import st.slex.messenger.ui.auth.LoginEngine
import st.slex.messenger.ui.auth.SendCodeEngine
import st.slex.messenger.utilites.result.AuthResponse
import st.slex.messenger.utilites.result.VoidResponse
import javax.inject.Inject

interface AuthInteractor {

    suspend fun login(phone: String): Flow<AuthResponse>
    suspend fun sendCode(id: String, code: String): Flow<AuthResponse>

    @ExperimentalCoroutinesApi
    class Base @Inject constructor(
        private val repository: AuthRepository,
        private val loginEngine: LoginEngine,
        private val sendCodeEngine: SendCodeEngine,
    ) : AuthInteractor {

        override suspend fun login(phone: String): Flow<AuthResponse> =
            loginEngine.login(phone).collectThis()

        override suspend fun sendCode(id: String, code: String): Flow<AuthResponse> =
            sendCodeEngine.sendCode(id, code).collectThis()


        private suspend fun Flow<AuthResponse>.collectThis(): Flow<AuthResponse> = callbackFlow {
            this@collectThis.collect { response ->
                response.collectionBase({
                    trySendBlocking(AuthResponse.Success)
                }, {
                    trySendBlocking(AuthResponse.Send(it))
                }, {
                    trySendBlocking(AuthResponse.Failure(it))
                })
            }
            awaitClose { }
        }

        private suspend inline fun AuthResponse.collectionBase(
            crossinline success: () -> Unit,
            crossinline sendCode: (String) -> Unit,
            crossinline failure: (Exception) -> Unit
        ) {
            when (this) {
                is AuthResponse.Success -> {
                    repository.saveUser().collect {
                        when (it) {
                            is VoidResponse.Success -> success()
                            is VoidResponse.Failure -> failure(it.exception)
                            else -> {
                            }
                        }
                    }
                }
                is AuthResponse.Send -> {
                    sendCode(id)
                }
                is AuthResponse.Failure -> {
                    failure(exception)
                }
                else -> {
                }
            }
        }


    }
}