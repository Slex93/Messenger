package st.slex.messenger.ui.core

sealed class VoidUIResponse {
    object Success : VoidUIResponse()
    class Failure(val exception: Exception) : VoidUIResponse()
    object Loading : VoidUIResponse()
}
