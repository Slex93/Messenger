package st.slex.messenger.main.data.chats

interface ChatsData {

    fun chatId(): String
    fun username(): String
    fun text(): String
    fun timestamp(): String
    fun from(): String

    data class Base(
        val from: String = "",
        val message: String = "",
        val timestamp: Any = "",
        val username: String = "",
        val full_name: String = "",
        val id: String = ""
    ) : ChatsData {
        override fun from(): String = from
        override fun chatId(): String = id
        override fun username(): String = full_name
        override fun text(): String = message
        override fun timestamp(): String = timestamp.toString()
    }
}
