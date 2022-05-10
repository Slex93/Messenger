package st.slex.messenger.auth.ui.use_case.interf

import st.slex.messenger.auth.core.LoginValue

interface SendCodeUseCase {
    suspend fun sendCode(id: String, code: String): LoginValue
}