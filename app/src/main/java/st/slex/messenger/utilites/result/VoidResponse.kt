package st.slex.messenger.utilites.result

sealed class VoidResponse {
    object Success : VoidResponse()
    class Failure(val exception: Exception) : VoidResponse()
    object Loading : VoidResponse()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success"
            is Failure -> "Failure[exception=$exception]"
            is Loading -> "Loading"
        }
    }
}