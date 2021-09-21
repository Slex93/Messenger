package st.slex.messenger.ui.chats

sealed class ChatsUIResult {
    data class Success(val data: List<ChatsUI>) : ChatsUIResult()
    data class Failure(val exception: Exception) : ChatsUIResult()
    object Loading : ChatsUIResult()
}