package st.slex.messenger.domain

sealed class LoginDomainResult {

    sealed class Success : LoginDomainResult() {
        object LogIn : LoginDomainResult.Success()
        class SendCode(val id: String) : LoginDomainResult.Success()
    }

    class Failure(val exception: Exception) : LoginDomainResult()
}
