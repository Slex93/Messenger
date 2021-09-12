package st.slex.messenger.data.repository

import st.slex.messenger.data.model.UserInitial

interface LoginRepository {

    suspend fun saveUser(user: UserInitial)
    val user: Any?
}