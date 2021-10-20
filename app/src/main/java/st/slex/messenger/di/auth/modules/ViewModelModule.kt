package st.slex.messenger.di.auth.modules

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.di.key.ViewModelKey
import st.slex.messenger.ui.auth.AuthViewModel

@Module
interface ViewModelModule {

    @ExperimentalCoroutinesApi
    @IntoMap
    @Binds
    @ViewModelKey(AuthViewModel::class)
    fun bindsLoginViewModel(viewModel: AuthViewModel): ViewModel
}