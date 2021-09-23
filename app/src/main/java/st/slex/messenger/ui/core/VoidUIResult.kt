package st.slex.messenger.ui.core

sealed class VoidUIResult {
    object Success : VoidUIResult()
    class Failure(val exception: Exception) : VoidUIResult()
    object Loading : VoidUIResult()
}
