package st.slex.messenger.data.repository.interf

import kotlinx.coroutines.flow.Flow
import st.slex.messenger.data.model.UserInitial

interface AuthRepository {

    suspend fun saveUser(user: UserInitial): Flow<Result<String>>
    val user: Any?
}