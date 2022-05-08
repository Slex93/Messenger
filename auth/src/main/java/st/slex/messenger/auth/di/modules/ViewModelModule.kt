package st.slex.messenger.auth.di.modules

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.auth.di.keys.ViewModelKey
import st.slex.messenger.auth.ui.AuthViewModel

@Module
interface ViewModelModule {

    @ExperimentalCoroutinesApi
    @IntoMap
    @Binds
    @ViewModelKey(AuthViewModel::class)
    fun bindsLoginViewModel(viewModel: AuthViewModel): ViewModel
}