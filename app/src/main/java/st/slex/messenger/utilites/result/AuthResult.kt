package st.slex.messenger.utilites.result

sealed class AuthResult<out R> {
    data class Success<out T>(val data: T) : AuthResult<T>()
    data class Send<out T>(val data: T) : AuthResult<T>()
    class Failure(val exception: String) : AuthResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Send<*> -> "Send[send=$data]"
            is Failure -> "Failure[exception=$exception]"
        }
    }
}