package st.slex.messenger.main.ui.single_chat

import st.slex.messenger.main.ui.core.AbstractView

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