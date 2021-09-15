package st.slex.messenger.domain.chats

import st.slex.messenger.ui.main_screen.ChatsUI

interface ChatsDomain {

    fun <T> map(mapper: ChatsDomainMapper<T>): T

    data class Base(
        private val from: String,
        private val text: String,
        private val timestamp: Any,
        private val username: String,
        private val full_name: String,
        private val url: String,
        private val id: String
    ) : ChatsDomain {
        override fun <T> map(mapper: ChatsDomainMapper<T>) =
            mapper.map(from, text, timestamp, username, full_name, url, id)
    }

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

}