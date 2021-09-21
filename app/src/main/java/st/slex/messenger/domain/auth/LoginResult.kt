package st.slex.messenger.domain.auth

sealed interface LoginResult {
    fun <T> map(mapper: AuthResultMapper<T>): T

    data class Success(private val profile: Map<String, Any>) : LoginResult {
        override fun <T> map(mapper: AuthResultMapper<T>): T = mapper.map(profile)
    }

    data class Failure(val exception: Exception) : LoginResult {
        override fun <T> map(mapper: AuthResultMapper<T>): T = mapper.map(emptyMap())
    }

    data class SendCode(private val id: String) : LoginResult {
        override fun <T> map(mapper: AuthResultMapper<T>): T = mapper.map(emptyMap())
    }
}
