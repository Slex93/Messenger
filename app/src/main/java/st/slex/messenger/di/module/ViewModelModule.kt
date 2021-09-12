package st.slex.messenger.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.di.key.ViewModelKey
import st.slex.messenger.ui.activities.ActivityViewModel
import st.slex.messenger.ui.auth.AuthViewModel
import st.slex.messenger.ui.auth.LoginViewModel
import st.slex.messenger.ui.contacts.ContactViewModel
import st.slex.messenger.ui.main_screen.MainScreenViewModel
import st.slex.messenger.ui.settings.SettingsViewModel
import st.slex.messenger.ui.single_chat.SingleChatViewModel

@ExperimentalCoroutinesApi
@Module
interface ViewModelModule {

    @IntoMap
    @Binds
    @ViewModelKey(ActivityViewModel::class)
    fun bindsMainActivityViewModel(viewModel: ActivityViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(MainScreenViewModel::class)
    fun bindsMainScreenViewModel(viewModel: MainScreenViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(AuthViewModel::class)
    fun bindsAuthViewModel(viewModel: AuthViewModel): ViewModel

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
    @ViewModelKey(LoginViewModel::class)
    fun bindsLoginViewModel(viewModel: LoginViewModel): ViewModel
}