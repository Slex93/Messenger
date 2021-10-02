package st.slex.messenger.data.core

sealed class VoidDataResult {
    object Success : VoidDataResult()
    class Failure(val exception: Exception) : VoidDataResult()
}