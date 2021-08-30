package st.slex.messenger.utilites.result

sealed class AuthResult {
    object Success : AuthResult()
    object Send : AuthResult()
    class Failure(val exception: Exception) : AuthResult()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success"
            is Send -> "Send"
            is Failure -> "Failure[exception=$exception]"
        }
    }
}