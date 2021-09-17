package st.slex.messenger.domain.user

sealed class UserDomainResult {

    abstract fun <T> map(mapper: UserDomainMapper<T>): T

    data class Success(val data: UserDomain) : UserDomainResult() {
        override fun <T> map(mapper: UserDomainMapper<T>): T = mapper.map(data)
    }

    data class Failure(private val exception: Exception) : UserDomainResult() {
        override fun <T> map(mapper: UserDomainMapper<T>) = mapper.map(exception)
    }
}
