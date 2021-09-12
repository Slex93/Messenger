package st.slex.messenger.data.repository.interf

import st.slex.messenger.data.model.UserInitial

interface AuthRepository {

    suspend fun saveUser(user: UserInitial)
    val user: Any?
}