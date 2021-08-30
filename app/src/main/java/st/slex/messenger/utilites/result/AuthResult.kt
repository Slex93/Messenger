package st.slex.messenger.utilites.result

sealed class AuthResult {
    object Success : AuthResult()
    class Send(val id: String) : AuthResult()
    class Failure(val exception: Exception) : AuthResult()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success"
            is Send -> "Send[id=$id]"
            is Failure -> "Failure[exception=$exception]"
        }
    }
}