package st.slex.messenger.auth.ui.core

import st.slex.messenger.auth.core.LoginValue

fun interface AuthCallback {
    fun result(loginValue: LoginValue)
}