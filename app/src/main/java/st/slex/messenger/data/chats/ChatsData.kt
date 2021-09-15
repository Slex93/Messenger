package st.slex.messenger.data.chats

import st.slex.messenger.domain.chats.ChatsDomain

interface ChatsData {
    fun <T> map(mapper: ChatsDataMapper<T>): T

    data class Base(
        private val from: String,
        private val text: String,
        private val timestamp: Any,
        private val username: String,
        private val full_name: String,
        private val url: String,
        private val id: String
    ) : ChatsData {

        override fun <T> map(mapper: ChatsDataMapper<T>) =
            mapper.map(from, text, timestamp, username, full_name, url, id)
    }

    interface ChatsDataMapper<T> {

        fun map(
            from: String,
            text: String,
            timestamp: Any,
            username: String,
            full_name: String,
            url: String,
            id: String
        ): T

        class Base : ChatsDataMapper<ChatsDomain> {
            override fun map(
                from: String,
                text: String,
                timestamp: Any,
                username: String,
                full_name: String,
                url: String,
                id: String
            ) = ChatsDomain.Base(from, text, timestamp, username, full_name, url, id)
        }
    }
}
