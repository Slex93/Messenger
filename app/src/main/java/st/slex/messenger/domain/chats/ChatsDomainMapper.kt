package st.slex.messenger.domain.chats

import st.slex.messenger.core.Abstract
import st.slex.messenger.ui.chats.ChatsUI
import st.slex.messenger.ui.chats.ChatsUIResult

interface ChatsDomainMapper<T> : Abstract.Mapper.DomainToUi<List<ChatsDomain>, T> {

    class Base : ChatsDomainMapper<ChatsUIResult> {
        override fun map(data: List<ChatsDomain>): ChatsUIResult {
            return ChatsUIResult.Success(data.map {
                ChatsUI.Base(
                    id = it.chatId(),
                    username = it.username(),
                    text = it.text(),
                    url = it.url(),
                    timestamp = it.timestamp()
                )
            })
        }

        override fun map(exception: Exception): ChatsUIResult {
            return ChatsUIResult.Failure(exception)
        }

    }
}