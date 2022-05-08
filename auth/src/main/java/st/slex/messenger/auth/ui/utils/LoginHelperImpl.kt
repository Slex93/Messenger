package st.slex.messenger.auth.ui.utils

import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import st.slex.messenger.auth.core.LoginValue
import st.slex.messenger.auth.ui.AuthActivity
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class LoginHelperImpl @Inject constructor(
    private val activity: AuthActivity
) : LoginHelper {

    override suspend fun login(phone: String): LoginValue =
        suspendCoroutine { continuation ->
            val callback = PhoneAuthCallback { continuation.resumeWith(Result.success(it)) }
            val phoneOptions = PhoneAuthOptions
                .newBuilder(Firebase.auth)
                .setActivity(activity)
                .setPhoneNumber(phone)
                .setTimeout(LOGIN_TIMEOUT, TimeUnit.SECONDS)
                .setCallbacks(callback)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(phoneOptions)
        }

    companion object {
        private const val LOGIN_TIMEOUT: Long = 60L
    }
}