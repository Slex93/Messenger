package st.slex.messenger.domain.chats

interface ChatsDomain {

    fun chatId(): String
    fun username(): String
    fun text(): String
    fun url(): String
    fun timestamp(): String

    data class Base(
        private val id: String,
        private val username: String,
        private val text: String,
        private val url: String,
        private val timestamp: String
    ) : ChatsDomain {
        override fun chatId(): String = id
        override fun username(): String = username
        override fun text(): String = text
        override fun url(): String = url
        override fun timestamp(): String = timestamp
    }
}