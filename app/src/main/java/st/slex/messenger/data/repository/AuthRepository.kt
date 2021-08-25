package st.slex.messenger.data.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import st.slex.messenger.data.model.AuthUserModel
import st.slex.messenger.utilites.result.AuthResult

interface AuthRepository {
    val userModel: LiveData<AuthResult<AuthUserModel>>
    suspend fun signInWithPhone(phone: String, activity: Activity)
    suspend fun sendCode(id: String, code: String)
    suspend fun authUser(authUserModel: AuthUserModel)
}