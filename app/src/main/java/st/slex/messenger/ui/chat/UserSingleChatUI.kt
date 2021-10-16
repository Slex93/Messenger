package st.slex.messenger.ui.chat

import st.slex.messenger.ui.core.AbstractView

interface UserSingleChatUI {

    fun bind(stateTextView: AbstractView.Text, nameTextView: AbstractView.Text)

    data class Base(
        private val state: String,
        private val fullName: String
    ) : UserSingleChatUI {

        override fun bind(stateTextView: AbstractView.Text, nameTextView: AbstractView.Text) {
            stateTextView.map(state)
            nameTextView.map(fullName)
        }
    }
}