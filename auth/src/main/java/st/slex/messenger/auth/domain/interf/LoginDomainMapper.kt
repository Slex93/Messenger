package st.slex.messenger.auth.domain.interf

import st.slex.messenger.auth.core.LoginValue
import st.slex.messenger.auth.ui.LoginUIResult

interface LoginDomainMapper {
    fun map(data: LoginValue): LoginUIResult
}