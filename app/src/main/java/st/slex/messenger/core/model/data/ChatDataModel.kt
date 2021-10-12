package st.slex.messenger.core.model.data

data class ChatDataModel(
    val user: UserDataModel,
    val lastMessage: String,
    val timestamp: String
)