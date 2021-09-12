package st.slex.messenger.domain.interactor.impl

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import st.slex.messenger.data.model.UserInitial
import st.slex.messenger.data.repository.interf.AuthRepository
import st.slex.messenger.domain.interactor.interf.AuthInteractor
import st.slex.messenger.ui.auth.engine.interf.LoginEngine
import st.slex.messenger.ui.auth.engine.interf.SendCodeEngine
import st.slex.messenger.utilites.result.AuthResponse
import javax.inject.Inject


@ExperimentalCoroutinesApi
class AuthInteractorImpl @Inject constructor(
    private val repository: AuthRepository,
    private val loginEngine: LoginEngine,
    private val sendCodeEngine: SendCodeEngine
) : AuthInteractor {

    override suspend fun login(phone: String, activity: Activity): Flow<AuthResponse> =
        callbackFlow {
            loginEngine.login(phone, activity).collect { response ->
                if (response is AuthResponse.Success) {
                    repository.saveUser(
                        UserInitial(
                            FirebaseAuth.getInstance().uid.toString(),
                            phone
                        )
                    ).collect { result ->
                        result.onSuccess {
                            trySendBlocking(AuthResponse.Success)
                        }.onFailure {
                            trySendBlocking(AuthResponse.Failure(Exception(it)))
                        }
                    }
                } else {
                    trySendBlocking(response)
                }
            }
            awaitClose { }
        }

    override suspend fun sendCode(id: String, code: String): Flow<AuthResponse> = callbackFlow {
        sendCodeEngine.sendCode(id, code).collect { response ->
            if (response is AuthResponse.Success) {
                repository.saveUser(
                    UserInitial(
                        FirebaseAuth.getInstance().currentUser?.phoneNumber.toString(),
                        id
                    )
                ).collect { result ->
                    result.onSuccess {
                        trySendBlocking(AuthResponse.Success)
                    }.onFailure {
                        trySendBlocking(AuthResponse.Failure(Exception(it)))
                    }
                }
            } else {
                trySendBlocking(response)
            }
        }
        awaitClose { }
    }
}