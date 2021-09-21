package st.slex.messenger.domain.chats

sealed interface ChatsDomainResult {
    fun <T> map(mapper: ChatsDomainMapper<T>): T

    data class Success(private val chats: List<ChatsDomain>) : ChatsDomainResult {
        override fun <T> map(mapper: ChatsDomainMapper<T>): T = mapper.map(chats)
    }

    data class Failure(private val exception: Exception) : ChatsDomainResult {
        override fun <T> map(mapper: ChatsDomainMapper<T>): T = mapper.map(exception)
    }
}