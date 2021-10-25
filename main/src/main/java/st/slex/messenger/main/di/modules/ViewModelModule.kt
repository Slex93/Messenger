package st.slex.messenger.main.di.modules

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.main.di.ViewModelKey
import st.slex.messenger.main.ui.chats.ChatsViewModel
import st.slex.messenger.main.ui.contacts.ContactViewModel
import st.slex.messenger.main.ui.main.ActivityViewModel
import st.slex.messenger.main.ui.settings.SettingsViewModel
import st.slex.messenger.main.ui.single_chat.SingleChatViewModel
import st.slex.messenger.main.ui.user_profile.UserViewModel

@ExperimentalCoroutinesApi
@Module
interface ViewModelModule {

    @IntoMap
    @Binds
    @ViewModelKey(ActivityViewModel::class)
    fun bindsMainActivityViewModel(viewModel: ActivityViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(ChatsViewModel::class)
    fun bindsMainScreenViewModel(viewModel: ChatsViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(ContactViewModel::class)
    fun bindsContactViewModel(viewModel: ContactViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(SingleChatViewModel::class)
    fun bindsSingleChatViewModel(viewModel: SingleChatViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(SettingsViewModel::class)
    fun bindsSettingsViewModel(viewModel: SettingsViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(UserViewModel::class)
    fun bindsUserViewModel(viewModel: UserViewModel): ViewModel
}