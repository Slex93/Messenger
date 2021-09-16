package st.slex.messenger.core


sealed interface TestResponse<out R> {
    data class Success<T>(val value: T) : TestResponse<T>
    class Failure(val exception: Exception) : TestResponse<Nothing>
    object Loading : TestResponse<Nothing>
}
