package st.slex.messenger.ui.auth.engine.impl

import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.ui.auth.engine.interf.SendCodeEngine
import st.slex.messenger.ui.auth.engine.interf.signInWithCredential
import st.slex.messenger.utilites.result.AuthResponse
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SendCodeEngineImpl @Inject constructor() : SendCodeEngine {
    override suspend fun sendCode(id: String, code: String): Flow<AuthResponse> = callbackFlow {
        val credential = PhoneAuthProvider.getCredential(id, code)
        signInWithCredential(credential,
            { trySendBlocking(AuthResponse.Success) },
            { trySendBlocking(AuthResponse.Failure(it)) })
        awaitClose {}
    }
}