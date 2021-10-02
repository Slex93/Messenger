package st.slex.messenger.domain.auth

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import st.slex.messenger.data.auth.AuthData
import st.slex.messenger.data.auth.AuthRepository
import st.slex.messenger.data.core.VoidDataResult
import st.slex.messenger.ui.auth.LoginEngine
import st.slex.messenger.ui.auth.SendCodeEngine
import javax.inject.Inject

interface AuthInteractor {

    suspend fun login(phone: String): Flow<LoginDomainResult>
    suspend fun sendCode(id: String, code: String): Flow<LoginDomainResult>

    @ExperimentalCoroutinesApi
    class Base @Inject constructor(
        private val repository: AuthRepository,
        private val loginEngine: LoginEngine,
        private val sendCodeEngine: SendCodeEngine,
    ) : AuthInteractor {

        override suspend fun login(phone: String): Flow<LoginDomainResult> =
            loginEngine.login(phone).collectThis()

        override suspend fun sendCode(id: String, code: String): Flow<LoginDomainResult> =
            sendCodeEngine.sendCode(id, code).collectThis()


        private suspend fun Flow<LoginDomainResult>.collectThis(): Flow<LoginDomainResult> =
            callbackFlow {
                this@collectThis.collect { response ->
                    response.collectionBase({
                        trySendBlocking(LoginDomainResult.Success)
                    }, {
                        trySendBlocking(LoginDomainResult.SendCode(it))
                    }, {
                        trySendBlocking(LoginDomainResult.Failure(it))
                    })
                }
                awaitClose { }
            }

        private suspend inline fun LoginDomainResult.collectionBase(
            crossinline success: () -> Unit,
            crossinline sendCode: (String) -> Unit,
            crossinline failure: (Exception) -> Unit
        ) {
            when (this) {
                is LoginDomainResult.Success -> {
                    repository.saveUser(
                        AuthData.Base(
                            id = Firebase.auth.uid.toString(),
                            phone = Firebase.auth.currentUser?.phoneNumber.toString()
                        )
                    ).collect {
                        when (it) {
                            is VoidDataResult.Success -> success()
                            is VoidDataResult.Failure -> failure(it.exception)
                        }
                    }
                }
                is LoginDomainResult.SendCode -> {
                    sendCode(id)
                }
                is LoginDomainResult.Failure -> {
                    failure(exception)
                }
            }
        }


    }
}