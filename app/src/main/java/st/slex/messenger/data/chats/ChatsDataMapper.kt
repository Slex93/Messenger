package st.slex.messenger.data.chats

import st.slex.messenger.core.Abstract
import st.slex.messenger.domain.chats.ChatsDomain
import st.slex.messenger.domain.chats.ChatsDomainResult

interface ChatsDataMapper<T> : Abstract.Mapper.DataToDomain<List<ChatsData>, T> {

    class Base : ChatsDataMapper<ChatsDomainResult> {
        override fun map(data: List<ChatsData>): ChatsDomainResult =
            ChatsDomainResult.Success(data.map {
                ChatsDomain.Base(
                    id = it.chatId(),
                    username = it.username(),
                    text = it.text(),
                    url = it.url(),
                    timestamp = it.timestamp()
                )
            })

        override fun map(exception: Exception): ChatsDomainResult {
            return ChatsDomainResult.Failure(exception)
        }
    }
}