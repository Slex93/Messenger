package st.slex.messenger.main.di.component

import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.main.di.scope.MainActivityScope
import st.slex.messenger.main.ui.MainActivity
import st.slex.messenger.main.ui.chats.ChatsFragment
import st.slex.messenger.main.ui.contacts.ContactFragment
import st.slex.messenger.main.ui.settings.SettingsFragment
import st.slex.messenger.main.ui.single_chat.SingleChatFragment
import st.slex.messenger.main.ui.user_profile.EditUsernameFragment
import st.slex.messenger.main.ui.user_profile.UserProfileFragment

@MainActivityScope
@ExperimentalCoroutinesApi
@Component(modules = [MainActivityModule::class])
interface MainActivityComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun activity(activity: MainActivity): Builder
        fun create(): MainActivityComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: SettingsFragment)
    fun inject(fragment: UserProfileFragment)
    fun inject(fragment: EditUsernameFragment)
    fun inject(fragment: SingleChatFragment)
    fun inject(fragment: ContactFragment)
    fun inject(fragment: ChatsFragment)
}