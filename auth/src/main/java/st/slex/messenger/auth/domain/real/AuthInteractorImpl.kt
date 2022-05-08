package st.slex.messenger.auth.domain.real

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import st.slex.core.Resource
import st.slex.messenger.auth.core.LoginValue
import st.slex.messenger.auth.domain.interf.AuthInteractor
import st.slex.messenger.auth.domain.interf.AuthRepository
import st.slex.messenger.auth.ui.SendCodeEngine
import st.slex.messenger.auth.ui.utils.LoginHelper
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AuthInteractorImpl @Inject constructor(
    private val repository: AuthRepository,
    private val loginEngine: LoginHelper,
    private val sendCodeEngine: SendCodeEngine,
) : AuthInteractor {

    override suspend fun login(phone: String): Flow<LoginValue> = flow {
        emit(loginEngine.login(phone))
    }.checkAuth()

    override suspend fun sendCode(id: String, code: String): Flow<LoginValue> = flow {
        emit(sendCodeEngine.sendCode(id, code))
    }.checkAuth()

    private suspend fun Flow<LoginValue>.checkAuth(): Flow<LoginValue> =
        flatMapLatest { loginResult ->
            if (loginResult is LoginValue.Success.LogIn) saveUserToDataBase()
            else flowOf(loginResult)
        }

    private suspend fun saveUserToDataBase(): Flow<LoginValue> =
        repository.saveUser().flatMapLatest {
            flowOf(it.mappedSaveUserResult)
        }

    private val Resource<Void>.mappedSaveUserResult: LoginValue
        get() = if (this is Resource.Failure) LoginValue.Failure(exception)
        else LoginValue.Success.LogIn
}