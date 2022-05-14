package st.slex.messenger.auth.domain.real

import st.slex.messenger.auth.core.LoginValue
import st.slex.messenger.auth.domain.interf.LoginDomainMapper
import st.slex.messenger.auth.ui.core.LoginUIResult
import javax.inject.Inject

class LoginDomainMapperImpl @Inject constructor() : LoginDomainMapper {

    override fun map(data: LoginValue): LoginUIResult = when (data) {
        is LoginValue.Success.LogIn -> LoginUIResult.Success.LogIn
        is LoginValue.Success.SendCode -> LoginUIResult.Success.SendCode(data.id)
        is LoginValue.Failure -> LoginUIResult.Failure(data.exception)
    }
}