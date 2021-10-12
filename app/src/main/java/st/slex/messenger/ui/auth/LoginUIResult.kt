package st.slex.messenger.ui.auth

sealed class LoginUIResult {

    sealed class Success : LoginUIResult() {
        object LogIn : LoginUIResult.Success()
        class SendCode(val id: String) : LoginUIResult.Success()
    }

    class Failure(val exception: Exception) : LoginUIResult()
    object Loading : LoginUIResult()
}