package st.slex.messenger.data.contacts

sealed interface ContactsDataResult {

    fun <T> map(mapper: ContactsDataMapper<T>): T

    data class Success(val data: List<ContactsData>) : ContactsDataResult {
        override fun <T> map(mapper: ContactsDataMapper<T>): T = mapper.map(data)
    }

    data class Failure(private val exception: Exception) : ContactsDataResult {
        override fun <T> map(mapper: ContactsDataMapper<T>) = mapper.map(exception)
    }
}