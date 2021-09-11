package st.slex.messenger.domain

import st.slex.messenger.data.LoginRepository
import st.slex.messenger.data.UserInitial
import st.slex.messenger.ui.auth.Auth
import st.slex.messenger.ui.auth.LoginEngine

interface LoginInteractor {

    fun authorized(): Boolean

    suspend fun login(loginEngine: LoginEngine): Auth
    suspend fun signIn(signIn: LoginEngine): Auth

    class Base(
        private val repository: LoginRepository,
        private val mapper: Auth.AuthResultMapper<UserInitial>,
    ) : LoginInteractor {

        override suspend fun login(loginEngine: LoginEngine): Auth = try {
            val result = loginEngine.login()
            repository.saveUser(result.map(mapper))
            result
        } catch (e: Exception) {
            Auth.Fail(e)
        }

        override suspend fun signIn(signIn: LoginEngine): Auth = try {
            signIn.login()
        } catch (e: Exception) {
            Auth.Fail(e)
        }

        override fun authorized() = repository.user() != null
    }
}