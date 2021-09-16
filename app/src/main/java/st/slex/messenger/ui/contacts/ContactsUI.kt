package st.slex.messenger.ui.contacts

import st.slex.messenger.ui.core.AbstractView
import st.slex.messenger.ui.core.CustomCardView

interface ContactsUI {

    fun map(
        userName: AbstractView.Text,
        userPhone: AbstractView.Text,
        userAvatar: AbstractView.Image,
        userCardView: AbstractView.Card
    )

    fun startChat(function: (CustomCardView, String) -> Unit)
    fun same(contactData: ContactsUI): Boolean

    data class Base(
        private val id: String = "",
        private val phone: String = "",
        private val full_name: String = "",
        private val url: String = ""
    ) : ContactsUI {
        override fun map(
            userName: AbstractView.Text,
            userPhone: AbstractView.Text,
            userAvatar: AbstractView.Image,
            userCardView: AbstractView.Card
        ) {
            userName.map(full_name)
            userPhone.map(phone)
            userAvatar.load(url)
            userCardView.transit(id)
            _cardView = userCardView.getCard()
        }

        override fun startChat(function: (CustomCardView, String) -> Unit) = function(cardView, url)

        override fun same(contactData: ContactsUI): Boolean =
            contactData is Base && contactData.id == id

        private var _cardView: CustomCardView? = null
        private val cardView get() = _cardView!!
    }
}