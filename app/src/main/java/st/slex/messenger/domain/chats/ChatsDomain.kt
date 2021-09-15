package st.slex.messenger.domain.chats

interface ChatsDomain {

    fun <T> map(mapper: ChatsDomainMapper<T>): T

    data class Base(
        private val from: String,
        private val text: String,
        private val timestamp: Any,
        private val username: String,
        private val full_name: String,
        private val url: String,
        private val id: String
    ) : ChatsDomain {
        override fun <T> map(mapper: ChatsDomainMapper<T>) =
            mapper.map(from, text, timestamp, username, full_name, url, id)
    }

}