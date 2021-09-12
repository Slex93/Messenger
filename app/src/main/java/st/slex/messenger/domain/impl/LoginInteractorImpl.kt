package st.slex.messenger.domain.impl

import st.slex.messenger.domain.interf.LoginEngine
import st.slex.messenger.domain.interf.LoginInteractor
import st.slex.messenger.domain.interf.SendCodeEngine
import javax.inject.Inject

class LoginInteractorImpl @Inject constructor(
    loginEngine: LoginEngine,
    sendCodeEngine: SendCodeEngine
) : LoginInteractor {

    override suspend fun login(phone: String) {
        TODO("Not yet implemented")
    }

    override suspend fun sendCode(id: String, code: String) {
        TODO("Not yet implemented")
    }
}