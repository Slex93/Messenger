package st.slex.messenger.domain.interactor.impl

import android.app.Activity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import st.slex.messenger.data.model.UserInitial
import st.slex.messenger.data.repository.LoginRepository
import st.slex.messenger.domain.engine.interf.LoginEngine
import st.slex.messenger.domain.engine.interf.SendCodeEngine
import st.slex.messenger.domain.interactor.interf.LoginInteractor
import st.slex.messenger.utilites.result.AuthResponse
import javax.inject.Inject

class LoginInteractorImpl @Inject constructor(
    private val repository: LoginRepository,
    private val loginEngine: LoginEngine,
    private val sendCodeEngine: SendCodeEngine
) : LoginInteractor {

    private fun user() = UserInitial(Firebase.auth.currentUser?.phoneNumber.toString())

    override suspend fun login(phone: String, activity: Activity): Flow<AuthResponse> =
        flow {
            loginEngine.login(phone, activity).collect {
                if (it is AuthResponse.Success) {
                    repository.saveUser(user())
                }
                emit(it)
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun sendCode(id: String, code: String): Flow<AuthResponse> = flow {
        sendCodeEngine.sendCode(id, code).collect {
            if (it is AuthResponse.Success) {
                repository.saveUser(user())
            }
            emit(it)
        }
    }.flowOn(Dispatchers.IO)
}