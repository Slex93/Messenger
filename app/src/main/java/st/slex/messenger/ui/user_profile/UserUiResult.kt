package st.slex.messenger.ui.user_profile

sealed class UserUiResult {
    data class Success(val data: UserUI) : UserUiResult()
    data class Failure(private val exception: Exception) : UserUiResult()
    object Loading : UserUiResult()
}