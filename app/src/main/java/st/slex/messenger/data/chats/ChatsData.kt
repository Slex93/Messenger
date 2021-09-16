package st.slex.messenger.data.chats

interface ChatsData {

    fun chatId(): String
    fun username(): String
    fun text(): String
    fun url(): String
    fun timestamp(): String

    data class Base(
        val from: String = "",
        val text: String = "",
        val timestamp: Any = "",
        val username: String = "",
        val full_name: String = "",
        val url: String = "",
        val id: String = ""
    ) : ChatsData {
        override fun chatId(): String = id
        override fun username(): String = full_name
        override fun text(): String = text
        override fun url(): String = url
        override fun timestamp(): String = timestamp.toString()
    }
}
