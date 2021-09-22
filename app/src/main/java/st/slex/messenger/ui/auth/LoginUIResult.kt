package st.slex.messenger.ui.auth

sealed class LoginUIResult {
    object Success : LoginUIResult()
    class SendCode(val id: String) : LoginUIResult()
    class Failure(val exception: Exception) : LoginUIResult()
    object Loading : LoginUIResult()
}