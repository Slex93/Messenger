package st.slex.messenger.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import st.slex.messenger.data.LoginRepository
import st.slex.messenger.data.UserInitial
import st.slex.messenger.ui.auth.Auth
import st.slex.messenger.ui.auth.LoginEngine
import st.slex.messenger.ui.auth.SendCodeEngine

@ExperimentalCoroutinesApi
interface LoginInteractor {

    fun authorized(): Boolean

    suspend fun login(loginEngine: LoginEngine, phone: String): Flow<Auth>
    suspend fun sendCode(sendCodeEngine: SendCodeEngine, id: String, code: String): Flow<Auth>

    class Base(
        private val repository: LoginRepository,
        private val mapper: Auth.AuthResultMapper<UserInitial>,
    ) : LoginInteractor {

        override suspend fun login(loginEngine: LoginEngine, phone: String): Flow<Auth> =
            callbackFlow {
                try {
                    val result = loginEngine.login(phone)
                    result.collect {
                        repository.saveUser(it.map(mapper))
                        trySendBlocking(it)
                    }
                } catch (e: Exception) {
                    trySendBlocking(Auth.Fail(e))
                }
                awaitClose { }
            }

        override suspend fun sendCode(
            sendCodeEngine: SendCodeEngine,
            id: String,
            code: String
        ): Flow<Auth> =
            callbackFlow {
                try {
                    val result = sendCodeEngine.sendCode(id, code)
                    result.collect {
                        repository.saveUser(it.map(mapper))
                        trySendBlocking(it)
                    }
                } catch (e: Exception) {
                    trySendBlocking(Auth.Fail(e))
                }
                awaitClose { }
            }

        override fun authorized() = repository.user() != null
    }
}