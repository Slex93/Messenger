package st.slex.messenger.utilites.result

sealed class AuthResponse {
    object Success : AuthResponse()
    class Send(val id: String) : AuthResponse()
    class Failure(val exception: Exception) : AuthResponse()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success"
            is Send -> "Send[id=$id]"
            is Failure -> "Failure[exception=$exception]"
        }
    }
}