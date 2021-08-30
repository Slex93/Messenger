package st.slex.messenger.data.model

data class MessageModel(
    val mid: String = "",
    val username: String = "",
    val content: String = "",
    val timestamp: String = "",
    val url: String = "",
    val user: UserModel = UserModel(),
)
