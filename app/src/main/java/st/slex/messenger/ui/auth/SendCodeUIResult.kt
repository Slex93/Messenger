package st.slex.messenger.ui.auth

import st.slex.messenger.domain.auth.UserInitial

sealed interface SendCodeUIResult {
    data class Success(val data: UserInitial) : SendCodeUIResult
    data class Failure(val exception: Exception) : SendCodeUIResult
    object Loading : SendCodeUIResult
}