package st.slex.messenger.ui.main_screen

sealed interface ChatsUIResult {
    data class Success(val data: List<ChatsUI>) : ChatsUIResult
    data class Failure(val exception: Exception) : ChatsUIResult
    object Loading : ChatsUIResult
}