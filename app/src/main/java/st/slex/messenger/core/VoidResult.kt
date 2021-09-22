package st.slex.messenger.core

sealed class VoidResult {
    object Success : VoidResult()
    class Failure(val exception: Exception) : VoidResult()
}