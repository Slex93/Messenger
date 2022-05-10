package st.slex.messenger.auth.ui.core

sealed class LoginUIResult {

    sealed class Success : LoginUIResult() {
        object LogIn : Success()
        class SendCode(val id: String) : Success()
    }

    class Failure(val exception: Exception) : LoginUIResult()
    object Loading : LoginUIResult()
}