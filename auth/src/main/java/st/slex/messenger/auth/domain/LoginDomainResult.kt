package st.slex.messenger.auth.domain

sealed class LoginDomainResult {

    sealed class Success : LoginDomainResult() {
        object LogIn : Success()
        class SendCode(val id: String) : Success()
    }

    class Failure(val exception: Exception) : LoginDomainResult()
}
