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
        private val id: String,
        private val username: String,
        private val text: String,
        private val url: String,
        private val timestamp: Any
    ) : ChatsUI {
        override fun map(
            userName: AbstractView.Text,
            userMessage: AbstractView.Text,
            userAvatar: AbstractView.Image,
            userTimestamp: AbstractView.Text,
            userCardView: AbstractView.Card
        ) {
            userName.map(username)
            userMessage.map(text)
            userAvatar.load(url)
            userTimestamp.map(timestamp.toString().convertToTime())
            userCardView.transit(id)
            _cardView = userCardView.getCard()
        }

        private var _cardView: CustomCardView? = null
        private val cardView get() = _cardView!!

        override fun startChat(function: (CustomCardView, String) -> Unit) =
            function(cardView, url)

        override fun same(userData: ChatsUI) = userData is Base && userData.id == id
    }
}