package st.slex.messenger.auth.ui.utils

import st.slex.messenger.auth.core.LoginValue

interface LoginHelper {
    suspend fun login(phone: String): LoginValue
}