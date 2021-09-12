package st.slex.messenger.domain.interactor.interf

import android.app.Activity
import kotlinx.coroutines.flow.Flow
import st.slex.messenger.utilites.result.AuthResponse

interface AuthInteractor {

    suspend fun login(phone: String, activity: Activity): Flow<AuthResponse>
    suspend fun sendCode(id: String, code: String): Flow<AuthResponse>
}