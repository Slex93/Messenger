package st.slex.messenger.auth.core

sealed class LoginValue {

    sealed class Success : LoginValue() {
        object LogIn : Success()
        class SendCode(val id: String) : Success()
    }

    class Failure(val exception: Exception) : LoginValue()
}