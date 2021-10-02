package st.slex.messenger.core

sealed class DataResult<out R> {
    data class Success<out T>(val value: T) : DataResult<T>()
    class Failure(val exception: Exception) : DataResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$value]"
            is Failure -> "Failure[exception=$exception]"
        }
    }
}
