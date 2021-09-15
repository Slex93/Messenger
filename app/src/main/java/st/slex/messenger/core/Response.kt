package st.slex.messenger.core

sealed class Response<out R> {
    data class Success<out T>(val value: T) : Response<T>()
    class Failure(val exception: Exception) : Response<Nothing>()
    object Loading : Response<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$value]"
            is Failure -> "Failure[exception=$exception]"
            is Loading -> "Loading"
        }
    }
}
