package st.slex.messenger.main.ui.chats

import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.main.ui.core.ClickListener

class ChatsItemClicker : ClickListener<ChatsUI> {

    @ExperimentalCoroutinesApi
    override fun click(item: ChatsUI) {
        item.startChat { card, url ->
            val directions =
                ChatsFragmentDirections.actionNavHomeToNavSingleChat(
                    card.transitionName,
                    url
                )
            val extras = FragmentNavigatorExtras(card to card.transitionName)
            card.findNavController().navigate(directions, extras)
        }
    }
}