package st.slex.messenger.domain.chats

import st.slex.messenger.ui.main_screen.ChatsUI

interface ChatsDomainMapper<T> {
    fun map(
        from: String,
        text: String,
        timestamp: Any,
        username: String,
        full_name: String,
        url: String,
        id: String
    ): T

    class Base : ChatsDomainMapper<ChatsUI> {
        override fun map(
            from: String,
            text: String,
            timestamp: Any,
            username: String,
            full_name: String,
            url: String,
            id: String
        ) = ChatsUI.Base(from, text, timestamp, username, full_name, url, id)
    }
}