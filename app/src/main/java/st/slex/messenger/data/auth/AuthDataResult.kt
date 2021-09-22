package st.slex.messenger.data.auth

sealed class AuthDataResult {
    object Success : AuthDataResult()
    class Failure(val exception: Exception) : AuthDataResult()
}
