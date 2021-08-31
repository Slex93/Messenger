package st.slex.messenger.data.model

data class ChatListModel(
    val user: UserModel = UserModel(),
    val message: MessageModel = MessageModel()
)
