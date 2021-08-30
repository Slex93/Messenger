package st.slex.messenger.data.repository.impl

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import st.slex.messenger.data.repository.interf.SettingsRepository
import st.slex.messenger.data.service.interf.StateService
import st.slex.messenger.utilites.result.VoidResponse
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val stateService: StateService
) : SettingsRepository {

    override suspend fun signOut(state: String): Flow<VoidResponse> = callbackFlow {
        val event = try {
            stateService.changeState(state).collect {
                when (it) {
                    is VoidResponse.Success -> {
                        trySendBlocking(VoidResponse.Success)
                        auth.signOut()
                    }
                    is VoidResponse.Failure -> {
                        trySendBlocking(VoidResponse.Failure(it.exception))
                    }
                    else -> {
                    }
                }
            }
        } catch (exception: Exception) {
            trySendBlocking(VoidResponse.Failure(exception))
        }
        awaitClose { event }
    }
}