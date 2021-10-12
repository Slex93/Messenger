package st.slex.messenger.core.model.firebase

data class FirebaseChatModel(
    val id: String? = "", //receiver
    val username: String? = "",
    val lastMessage: String? = "",
    val timestamp: String? = ""
)