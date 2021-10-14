package st.slex.messenger.domain

import st.slex.messenger.ui.auth.LoginUIResult
import javax.inject.Inject

interface LoginDomainMapper {

    fun map(data: LoginDomainResult): LoginUIResult

    class Base @Inject constructor() : LoginDomainMapper {
        override fun map(data: LoginDomainResult): LoginUIResult = when (data) {
            is LoginDomainResult.Success.LogIn -> LoginUIResult.Success.LogIn
            is LoginDomainResult.Success.SendCode -> LoginUIResult.Success.SendCode(data.id)
            is LoginDomainResult.Failure -> LoginUIResult.Failure(data.exception)
        }
    }
}