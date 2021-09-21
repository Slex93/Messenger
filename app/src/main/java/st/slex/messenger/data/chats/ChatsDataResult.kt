package st.slex.messenger.data.chats

sealed interface ChatsDataResult {

    fun <T> map(mapper: ChatsDataMapper<T>): T

    data class Success(val data: List<ChatsData>) : ChatsDataResult {
        override fun <T> map(mapper: ChatsDataMapper<T>): T = mapper.map(data)
    }

    data class Failure(private val exception: Exception) : ChatsDataResult {
        override fun <T> map(mapper: ChatsDataMapper<T>) = mapper.map(exception)
    }
}