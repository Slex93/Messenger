package st.slex.messenger.domain.impl

import android.app.Activity
import st.slex.messenger.domain.Auth
import st.slex.messenger.domain.interf.LoginEngine
import javax.inject.Inject

class LoginEngineImpl @Inject constructor(activity: Activity) : LoginEngine {

    override suspend fun login(phone: String): Auth {
        return Auth.SendCode("")
    }
}