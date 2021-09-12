package st.slex.messenger.domain.interf

interface LoginInteractor {

    suspend fun login(phone: String)
    suspend fun sendCode(id: String, code: String)
}