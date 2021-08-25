package st.slex.messenger.utilites.result

sealed class Resource<out R> {
    data class Success<out T>(val data: T) : Resource<T>()
    class Failure(val exception: Exception) : Resource<Nothing>()
    object Loading : Resource<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Failure -> "Failure[exception=$exception]"
            is Loading -> "Loading"
        }
    }
}
