package st.slex.messenger.auth.ui.use_case.interf

import st.slex.messenger.auth.core.LoginValue

interface LoginUseCase {
    suspend fun login(phone: String): LoginValue
}