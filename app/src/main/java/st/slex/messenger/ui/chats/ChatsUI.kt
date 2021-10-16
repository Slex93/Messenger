package st.slex.messenger.ui.chats

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

    fun getSender(): String
    fun startChat(function: (CustomCardView, String) -> Unit)
    fun same(userData: ChatsUI): Boolean
    fun gettingId(): String

    fun copy(
        id: String? = null,
        from: String? = null,
        message: String? = null,
        timestamp: String? = null,
        full_name: String? = null,
        url: String? = null
    ): ChatsUI

    data class Base(
        val id: String = "",
        val from: String = "",
        val message: String = "",
        val timestamp: Any = "",
        val full_name: String = "",
        val url: String = "",
    ) : ChatsUI {

        override fun getSender(): String = from
        override fun gettingId(): String = id
        override fun map(
            userName: AbstractView.Text,
            userMessage: AbstractView.Text,
            userAvatar: AbstractView.Image,
            userTimestamp: AbstractView.Text,
            userCardView: AbstractView.Card
        ) {
            userName.map(full_name)
            userMessage.map(message)
            userAvatar.load(url)
            userTimestamp.map(timestamp.toString().convertToTime())
            userCardView.transit(id)
            _cardView = userCardView.getCard()
        }

        private var _cardView: CustomCardView? = null
        private val cardView get() = _cardView!!

        override fun startChat(function: (CustomCardView, String) -> Unit) =
            function(cardView, url)

        override fun same(userData: ChatsUI) = userData is Base && userData.from == from
        override fun copy(
            id: String?,
            from: String?,
            message: String?,
            timestamp: String?,
            full_name: String?,
            url: String?
        ): ChatsUI = Base(
            id = id ?: this.id,
            from = from ?: this.from,
            message = message ?: this.message,
            timestamp = timestamp ?: this.timestamp,
            full_name = full_name ?: this.full_name,
            url = url ?: this.url
        )
    }
}