package st.slex.messenger.ui.main_screen

import st.slex.messenger.ui.core.AbstractView

interface ChatsUI {

    fun map(
        userName: AbstractView.Text,
        userId: AbstractView.Text,
        userAvatar: AbstractView.Image
    )

    fun get(
        username: String,
        id: String,
        url: String,
        timestamp: Any
    ): BasicChatList

    fun same(userData: ChatsUI): Boolean

    data class Base(
        private val from: String,
        private val text: String,
        private val timestamp: Any,
        private val username: String,
        private val full_name: String,
        private val url: String,
        private val id: String
    ) : ChatsUI {
        override fun map(
            userName: AbstractView.Text,
            userId: AbstractView.Text,
            userAvatar: AbstractView.Image
        ) {
            userName.map(full_name)
            userId.map(id)
            userAvatar.load(url)
        }

        override fun get(
            username: String,
            id: String,
            url: String,
            timestamp: Any
        ) = BasicChatList(username, id, url, timestamp)

        override fun same(userData: ChatsUI) = userData is Base && userData.id == id
    }
}