package st.slex.messenger.domain.auth

sealed interface SendCodeResult {
    fun <T> map(mapper: AuthResultMapper<T>): T

    data class Success(private val profile: Map<String, Any>) : SendCodeResult {
        override fun <T> map(mapper: AuthResultMapper<T>): T = mapper.map(profile)
    }

    data class Failure(val exception: Exception) : SendCodeResult {
        override fun <T> map(mapper: AuthResultMapper<T>): T = mapper.map(exception)
    }
}