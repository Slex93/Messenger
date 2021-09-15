package st.slex.messenger.data.repository.interf

import kotlinx.coroutines.flow.Flow
import st.slex.messenger.core.Response
import st.slex.messenger.data.model.UserModel
import st.slex.messenger.utilites.result.VoidResponse

interface UserRepository {
    suspend fun getCurrentUser(): Flow<Response<UserModel>>
    suspend fun saveUsername(username: String): Flow<VoidResponse>
}