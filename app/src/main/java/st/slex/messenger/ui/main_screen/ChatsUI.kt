package st.slex.messenger.ui.main_screen

import st.slex.messenger.ui.core.AbstractView
import st.slex.messenger.ui.core.CustomCardView
import st.slex.messenger.utilites.funs.convertToTime

interface ChatsUI {

    fun map(
        userName: AbstractView.Text,
        userMessage: AbstractView.Text,
        userAvatar: AbstractView.Image,
        userTimestamp: AbstractView.Text,
        userCardView: AbstractView.Card
    )

    fun startChat(function: (CustomCardView, String) -> Unit)
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
            userMessage: AbstractView.Text,
            userAvatar: AbstractView.Image,
            userTimestamp: AbstractView.Text,
            userCardView: AbstractView.Card
        ) {
            userName.map(full_name)
            userMessage.map(text)
            userAvatar.load(url)
            userTimestamp.map(timestamp.toString().convertToTime())
            userCardView.transit(id)
        }

        override fun startChat(function: (CustomCardView, String) -> Unit) = Unit

        override fun same(userData: ChatsUI) = userData is Base && userData.id == id
    }
}