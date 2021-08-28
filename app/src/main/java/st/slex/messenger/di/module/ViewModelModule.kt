package st.slex.messenger.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import st.slex.messenger.ActivityViewModel
import st.slex.messenger.di.key.ViewModelKey
import st.slex.messenger.ui.auth.AuthViewModel
import st.slex.messenger.ui.contacts.ContactViewModel
import st.slex.messenger.ui.main_screen.MainScreenViewModel

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
}