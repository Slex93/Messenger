package st.slex.messenger.auth.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import st.slex.messenger.auth.data.AuthRepository
import st.slex.messenger.auth.ui.LoginEngine
import st.slex.messenger.auth.ui.SendCodeEngine
import st.slex.messenger.core.Resource
import javax.inject.Inject

interface AuthInteractor {

    suspend fun login(phone: String): Flow<LoginDomainResult>
    suspend fun sendCode(id: String, code: String): Flow<LoginDomainResult>

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    class Base @Inject constructor(
        private val repository: AuthRepository,
        private val loginEngine: LoginEngine,
        private val sendCodeEngine: SendCodeEngine,
    ) : AuthInteractor {

        override suspend fun login(phone: String): Flow<LoginDomainResult> = flow {
            emit(loginEngine.login(phone))
        }.checkAuth()

        override suspend fun sendCode(id: String, code: String): Flow<LoginDomainResult> = flow {
            emit(sendCodeEngine.sendCode(id, code))
        }.checkAuth()

        private suspend fun Flow<LoginDomainResult>.checkAuth(): Flow<LoginDomainResult> = flow {
            this@checkAuth.collect { loginResult ->
                if (loginResult is LoginDomainResult.Success.LogIn) saveUser()
                else emit(loginResult)
            }
        }

        private suspend fun saveUser(): Flow<LoginDomainResult> =
            repository.saveUser().flatMapLatest {
                flowOf(it.mappedSaveUserResult)
            }

        private val Resource<Void>.mappedSaveUserResult: LoginDomainResult
            get() = if (this is Resource.Failure) {
                LoginDomainResult.Failure(exception)
            } else LoginDomainResult.Success.LogIn
    }
}