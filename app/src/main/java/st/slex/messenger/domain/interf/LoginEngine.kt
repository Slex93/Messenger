package st.slex.messenger.domain.interf

import st.slex.messenger.domain.Auth

interface LoginEngine {
    suspend fun login(phone: String): Auth
}