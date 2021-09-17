package st.slex.messenger.data.profile

sealed class UserDataResult {

    abstract fun <T> map(mapper: UserDataMapper<T>): T

    data class Success(val data: UserData) : UserDataResult() {
        override fun <T> map(mapper: UserDataMapper<T>): T = mapper.map(data)
    }

    data class Failure(private val exception: Exception) : UserDataResult() {
        override fun <T> map(mapper: UserDataMapper<T>) = mapper.map(exception)
    }
}