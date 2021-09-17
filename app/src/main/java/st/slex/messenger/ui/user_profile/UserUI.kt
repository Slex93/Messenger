package st.slex.messenger.ui.user_profile

import st.slex.messenger.ui.core.AbstractView

interface UserUI {

    fun map(
        phoneNumber: AbstractView.Text,
        userName: AbstractView.Text,
        avatar: AbstractView.Image,
        bioText: AbstractView.Text,
        fullName: AbstractView.Text,
        stateText: AbstractView.Text
    )

    data class Base(
        private val id: String = "",
        private val phone: String = "",
        private val username: String = "",
        private val url: String = "",
        private val bio: String = "",
        private val full_name: String = "",
        private val state: String = "",
    ) : UserUI {

        override fun map(
            phoneNumber: AbstractView.Text,
            userName: AbstractView.Text,
            avatar: AbstractView.Image,
            bioText: AbstractView.Text,
            fullName: AbstractView.Text,
            stateText: AbstractView.Text
        ) {
            phoneNumber.map(phone)
            userName.map(username)
            avatar.load(url)
            bioText.map(bio)
            fullName.map(full_name)
            stateText.map(state)
        }
    }
}