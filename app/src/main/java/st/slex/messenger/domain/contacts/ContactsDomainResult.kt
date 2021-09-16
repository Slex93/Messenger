package st.slex.messenger.domain.contacts

sealed class ContactsDomainResult {
    abstract fun <T> map(mapper: ContactsDomainMapper<T>): T

    data class Success(val data: List<ContactsDomain.Base>) : ContactsDomainResult() {
        override fun <T> map(mapper: ContactsDomainMapper<T>): T = mapper.map(data)
    }

    data class Failure(private val exception: Exception) : ContactsDomainResult() {
        override fun <T> map(mapper: ContactsDomainMapper<T>) = mapper.map(exception)
    }
}
